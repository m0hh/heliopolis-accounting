package com.helioplis.accounting.shift;

import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftRepo extends JpaRepository<Shift,Integer> {
    @Query(value = "SELECT EXISTS( SELECT 1 FROM shifts WHERE closed IS FALSE)", nativeQuery = true)
    Boolean findOpenShift();

    @Query(value = "SELECT EXISTS( SELECT 1 FROM shifts WHERE closed IS FALSE) OR " +
            "EXISTS (SELECT 1 FROM shifts WHERE (created_at <= :startTime    AND  closed_at > :startTime))"
            , nativeQuery = true)
    Boolean findOpenOrOverlappingShift(@Param("startTime") LocalDateTime startTime);

    @Modifying
    @Query(value ="UPDATE shifts "+
            "SET " +
            "total_orders = subquery.total_query_orders, " +
            "total_credits = subquery.total_query_credits, " +
            "total_expenses = subquery.total_query_expenses, " +
            "total_shift = subquery.total_query_orders - subquery.total_query_credits - subquery.total_query_expenses, " +
            "closed_at = NOW(), " +
            "closed = TRUE " +
            "FROM ( " +
            "SELECT " +
            "        (SELECT COALESCE(SUM(o.amount), 0) AS total_query_orders FROM orders o WHERE o.shift_id = :shiftId ), " +
            "(SELECT COALESCE(SUM(c.amount), 0) AS total_query_credits FROM credits c WHERE c.shift_id = :shiftId ), " +
            "(SELECT COALESCE(SUM(e.amount), 0) AS total_query_expenses FROM expenses e WHERE e.shift_id = :shiftId ) " +
            ") AS subquery " +
            "WHERE shifts.id = :shiftId "
            , nativeQuery = true)
    Integer closeShift(@Param("shiftId") Integer shiftId);

    @Query(value = "SELECT * FROM shifts WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate)" +
            " AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate)" +
            " AND (cast(:userId as INTEGER) IS NULL OR user_id_opened = :userId) ORDER BY closed ASC",
            countQuery = "SELECT * FROM shifts WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate)" +
                    " AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate)" +
                    " AND (cast(:userId as INTEGER) IS NULL OR user_id_opened = :userId) ORDER BY closed ASC",
            nativeQuery = true)
    List<Shift> findFilter(@Param("beforeDate") LocalDateTime beforeDate, @Param("afterDate") LocalDateTime afterDate, @Param("userId") Integer userId, Pageable pageable);
}
