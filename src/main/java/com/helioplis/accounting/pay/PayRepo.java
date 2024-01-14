package com.helioplis.accounting.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepo extends JpaRepository<Pay, Integer> {
}
