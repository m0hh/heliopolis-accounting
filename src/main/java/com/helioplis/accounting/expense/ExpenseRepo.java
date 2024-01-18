package com.helioplis.accounting.expense;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Integer> {
    @Query(value = "SELECT * FROM expenses WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate) AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate) AND (cast(:shiftId as INTEGER) IS NULL OR shift_id = :shiftId)",
            countQuery = "SELECT count(*) FROM expenses WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate) AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate) AND (cast(:shiftId as INTEGER) IS NULL OR shift_id = :shiftId)",
            nativeQuery = true)
    List<Expense> findFilter(@Param("beforeDate") LocalDateTime beforeDate, @Param("afterDate") LocalDateTime afterDate, @Param("shiftId") Integer shiftId, Pageable pageable);
}