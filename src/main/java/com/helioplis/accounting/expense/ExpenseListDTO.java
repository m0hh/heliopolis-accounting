package com.helioplis.accounting.expense;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseListDTO {
    private Integer id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
}
