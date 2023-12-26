package com.helioplis.accounting.order;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    @Id
    @SequenceGenerator(name = "order_id_seq",sequenceName = "order_id_seq",allocationSize = 1)
    @GeneratedValue(
            generator = "order_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    private Integer orderId;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Digits(integer=10, fraction=5, message = "You must enter a number")
    @NotNull(message = "amount cannot bel blank or null")
    @Column(name = "amount", precision = 10, scale = 2,nullable = false)
    private BigDecimal amount;
}
