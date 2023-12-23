package com.helioplis.accounting.credit;

import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.expense.ExpenseRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class CreditService {
    private final CreditRepo creditRepo;

    public void addNewCredit(Credit credit){
        creditRepo.save(credit);
    }

    List<Credit> listCredits(LocalDateTime start_date, LocalDateTime end_date)
    {
        return creditRepo.findDateBetween(start_date,end_date);

    }
}
