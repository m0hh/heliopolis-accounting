package com.helioplis.accounting.shift;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.helioplis.accounting.credit.Credit;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.order.Order;
import com.helioplis.accounting.pay.Pay;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shifts")
@Data
public class Shift {
    @Id
    @SequenceGenerator(name = "shifts_id_seq", sequenceName ="shifts_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "shifts_id_seq")
    private Integer id;


    @ManyToOne
    @JoinColumn(
            name = "user_id_opened",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_shift_user_open"),
            nullable = false
    )
    private UserHelioplis userOpen;


    @OneToMany(
            mappedBy = "shift",
            orphanRemoval = false,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(
            mappedBy = "shift",
            orphanRemoval = false,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Credit> credits = new ArrayList<>();

    @OneToMany(
            mappedBy = "shift",
            orphanRemoval = false,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @Digits(integer=10, fraction=2, message = "You must enter a number")
    @Column(name = "total_orders", precision = 10, scale = 2,nullable = true)
    private BigDecimal totalOrders;

    @Digits(integer=10, fraction=2, message = "You must enter a number")
    @Column(name = "total_credits", precision = 10, scale = 2,nullable = true)
    private BigDecimal totalCredits;

    @Digits(integer=10, fraction=2, message = "You must enter a number")
    @Column(name = "total_expenses", precision = 10, scale = 2,nullable = true)
    private BigDecimal totalExpenses;

    @Digits(integer=10, fraction=2, message = "You must enter a number")
    @Column(name = "total_shift", precision = 10, scale = 2,nullable = true)
    private BigDecimal totalShift;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "closed_at",nullable = true)
    private LocalDateTime closed_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "pay_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_pay_id"),
            nullable = true
    )
    @JsonBackReference
    private Pay pay;

    @Column(name = "closed")
    private boolean closed = false;




    public void addExpense(Expense expense){
        if (!this.expenses.contains(expense)){
            this.expenses.add(expense);
            expense.setShift(this);
        }
    }

    public void removeExpense(Expense expense){
        if(this.expenses.contains(expense)){
            this.expenses.remove(expense);
            expense.setShift(null);
        }
    }
    public void addCredit(Credit credit){
        if (!this.credits.contains(credit)){
            this.credits.add(credit);
            credit.setShift(this);
        }
    }

    public void removeCredit(Credit credit){
        if(this.credits.contains(credit)){
            this.credits.remove(credit);
            credit.setShift(null);
        }
    }

    public void addOrder(Order order){
        if (!this.orders.contains(order)){
            this.orders.add(order);
            order.setShift(this);
        }
    }

    public void removeOrder(Order order){
        if(this.orders.contains(order)){
            this.orders.remove(order);
            order.setShift(null);
        }
    }
    public void setOrders(List<Order> orders) {
        for (Order order : orders) {
            order.setShift(this);
        }
        this.orders = orders;
    }

    public String getUserOpen() {
        return userOpen.getUsername();
    }
}
