package com.helioplis.accounting.credit;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreditListDTO {
    private Integer id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
}
