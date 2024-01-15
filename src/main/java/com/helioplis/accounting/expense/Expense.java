package com.helioplis.accounting.expense;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.validator.DateConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
public class Expense {
    @Id
    @SequenceGenerator(name = "expenses_id_seq",sequenceName = "expenses_id_seq",allocationSize = 1)
    @GeneratedValue(
            generator = "expenses_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_expense_user"),
            nullable = false

    )
    private UserHelioplis user;

    @Digits(integer=10, fraction=5, message = "You must enter a number")
    @NotNull(message = "amount cannot bel blank or null")
    @Column(name = "amount", precision = 10, scale = 2,nullable = false)
    private BigDecimal amount;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shift_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "shift_id_expense_fk"),
            nullable = false
    )
    @NotNull(message = "You must enter a Shift id")
    @JsonBackReference
    private Shift shift;

    public Expense(UserHelioplis user, BigDecimal amount, String description, LocalDateTime createdAt, Shift shift) {
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.shift = shift;
    }

    public String getUser() {
        return user.getUsername();
    }
}
