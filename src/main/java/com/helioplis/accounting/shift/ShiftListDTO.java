package com.helioplis.accounting.shift;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ShiftListDTO {
    private Integer id;
    private String userOpen;
    private Boolean closed;
    private BigDecimal totalShift;
    private LocalDateTime createdAt;
    private LocalDateTime closed_at;
}