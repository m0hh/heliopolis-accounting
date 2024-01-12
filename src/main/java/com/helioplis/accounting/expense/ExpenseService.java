package com.helioplis.accounting.expense;


import com.helioplis.accounting.credit.Credit;
import com.helioplis.accounting.credit.CreditUpdateDTO;
import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@AllArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final ShiftRepo shiftRepo;
    private final  ExpenseMapper mapper;

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

    public Expense updateExpense(ExpenseUpdateDTO dto, Integer userId) {
        Expense myExpense = expenseRepo.findById(dto.getId()).orElseThrow(() -> new ApiRequestException("No Expense with that ID"));
        if (myExpense.getUser().getId() != userId){
            throw  new ApiRequestException("Only the user who created the expense can modify it");
        }
        if (dto.getShift() != null){
            Integer shiftId = dto.getShift().getId();
            Shift shift = shiftRepo.findById(shiftId).orElseThrow(()-> new ApiRequestException("No Shift by that ID"));
            if (shift.getClosed_at() != null){
                throw new ApiRequestException("This Shift is closed open it first and then modify");
            }

        }
        mapper.updateExpenseFromDto(dto, myExpense);

        return expenseRepo.save(myExpense);
    }

    public Expense retrieveExpense(Integer expenseId){
        return expenseRepo.findById(expenseId).orElseThrow(() -> new ApiRequestException("No Expense with that ID"));
    }

    @Transactional
    public void deleteCredit(Integer expenseId, Principal principal){
        Expense expense = expenseRepo.findById(expenseId).orElseThrow(()-> new ApiRequestException("No expense with that ID"));

        if (!expense.getUser().getUsername().equals(principal.getName())){
            throw new ApiRequestException("Only the user who created the expense can delete it");
        }
        expenseRepo.delete(expense);
    }

}
