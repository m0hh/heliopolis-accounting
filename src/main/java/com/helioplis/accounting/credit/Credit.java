package com.helioplis.accounting.credit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.shift.Shift;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "credits")
@Data
@NoArgsConstructor
public class Credit {
    @Id
    @SequenceGenerator(name = "credits_id_seq",sequenceName = "credits_id_seq",allocationSize = 1)
    @GeneratedValue(
            generator = "credits_id_seq"
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
            foreignKey = @ForeignKey(name = "fk_credits_user"),
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

    @ManyToOne()
    @JoinColumn(
            name = "shift_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "shift_id_credit_fk"),
            nullable = false
    )
    @NotNull
    private Shift shift;

    public Credit(UserHelioplis user,
                  @JsonProperty("amount") BigDecimal amount,
                  @JsonProperty("description") String description,
                  @JsonProperty("createdAt") LocalDateTime createdAt,
                  Shift shift) {
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.shift = shift;
    }
}


