package com.helioplis.accounting.credit;

import com.helioplis.accounting.expense.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditRepo extends JpaRepository<Credit,Integer> {
    @Query(value = "SELECT * FROM credits WHERE (cast(:beforeDate as timestamp without time zone) IS NULL OR created_at >= :beforeDate) AND (cast(:afterDate as timestamp without time zone) IS NULL OR created_at <= :afterDate)", nativeQuery = true)
    List<Credit> findDateBetween(@Param("beforeDate") LocalDateTime beforeDate, @Param("afterDate") LocalDateTime afterDate);
}
