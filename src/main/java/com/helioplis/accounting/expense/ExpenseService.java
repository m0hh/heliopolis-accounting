package com.helioplis.accounting.expense;


import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@AllArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final ShiftRepo shiftRepo;

    public Expense addNewExpense(Expense expense){
        Shift shift =  expense.getShift();
        shiftRepo.findById(shift.getId()).orElseThrow(() -> new ApiRequestException("No Shift with that Id"));
        return expenseRepo.save(expense);
    }

    List<Expense> listExpenses(String s_date, String e_date,Integer shiftId)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start_date = null;
        LocalDateTime end_date = null;
        try {
            if (s_date != null) {
                start_date = LocalDateTime.parse(s_date, formatter);
            }
            if(e_date != null) {
                end_date = LocalDateTime.parse(e_date, formatter);
            }
        } catch (DateTimeParseException e){
            throw new ApiRequestException("Wrong date format, the correct format is yyyy-MM-ddTHH:mm:ss", e);
        }
        return expenseRepo.findFilter(start_date,end_date,shiftId);

    }


}
