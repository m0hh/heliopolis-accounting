package com.helioplis.accounting.order;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.helioplis.accounting.shift.Shift;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @SequenceGenerator(name = "orders_id_seq",sequenceName = "orders_id_seq",allocationSize = 1)
    @GeneratedValue(
            generator = "orders_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(name = "order_id",nullable = false)
    private Integer orderId;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Digits(integer=10, fraction=5, message = "You must enter a number")
    @NotNull(message = "amount cannot bel blank or null")
    @Column(name = "amount", precision = 10, scale = 2,nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shift_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "shift_id_order_fk"),
            nullable = false
    )
    @NotNull
    @JsonBackReference
    private Shift shift;

}
