package com.helioplis.accounting.shift;

import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftRepo extends JpaRepository<Shift,Integer> {
    @Query(value = "SELECT EXISTS( SELECT 1 FROM shifts WHERE closed_at IS NULL)", nativeQuery = true)
    Boolean findOpenShift();

}
