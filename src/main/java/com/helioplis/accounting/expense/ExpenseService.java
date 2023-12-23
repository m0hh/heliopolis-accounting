package com.helioplis.accounting.expense;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepo expenseRepo;

    public void addNewExpense(Expense expense){
        expenseRepo.save(expense);
    }

    List<Expense> listExpenses(LocalDateTime start_date, LocalDateTime end_date)
    {
        return expenseRepo.findDateBetween(start_date,end_date);

    }


}
