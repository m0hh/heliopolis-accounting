package com.helioplis.accounting.pay;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.shift.Shift;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pay")
@Data
public class Pay {
    @Id
    @SequenceGenerator(name = "pay_id_seq", sequenceName ="pay_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "pay_id_seq")
    private Integer id;

    @Column(name = "total_hours", precision = 5, scale = 5,nullable = true)
    private BigDecimal totalHours;

    @Column(name = "total_pay", precision = 5, scale = 5,nullable = true)
    private BigDecimal totalPay;

    @Column(name = "total_deduction", precision = 5, scale = 5,nullable = true)
    private BigDecimal totalDeduction;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;


    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_pay_user"),
            nullable = false
    )
    private UserHelioplis user;

    @OneToMany(
            mappedBy = "pay",
            orphanRemoval = false,
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<Shift> shifts = new ArrayList<>();

    public void addShift(Shift shift){
        if (!this.shifts.contains(shift)){
            this.shifts.add(shift);
            shift.setPay(this);
        }
    }

    public void removeShift(Shift shift){
        if(this.shifts.contains(shift)){
            this.shifts.remove(shift);
            shift.setPay(null);
        }
    }
}
