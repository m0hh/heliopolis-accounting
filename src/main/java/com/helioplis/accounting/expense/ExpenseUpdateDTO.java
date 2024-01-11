package com.helioplis.accounting.expense;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.shift.Shift;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseUpdateDTO {

    private Integer id;

    private BigDecimal amount;

    private String description;


    private Shift shift;
}
