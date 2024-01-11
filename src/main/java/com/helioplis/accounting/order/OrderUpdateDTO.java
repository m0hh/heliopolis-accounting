package com.helioplis.accounting.order;

import com.helioplis.accounting.shift.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {

    private Integer id;

    private BigDecimal amount;


    private Shift shift;
}
