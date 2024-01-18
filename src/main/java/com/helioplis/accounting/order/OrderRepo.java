package com.helioplis.accounting.order;

import com.helioplis.accounting.expense.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT * FROM orders WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate) AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate) AND (cast(:shiftId as INTEGER) IS NULL OR shift_id = :shiftId)",
            countQuery = "SELECT count(*) FROM orders WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate) AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate) AND (cast(:shiftId as INTEGER) IS NULL OR shift_id = :shiftId)",
            nativeQuery = true)
    List<Order> findFilter(@Param("beforeDate") LocalDateTime beforeDate, @Param("afterDate") LocalDateTime afterDate, @Param("shiftId") Integer shiftId, Pageable pageable);
}
