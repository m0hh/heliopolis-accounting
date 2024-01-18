package com.helioplis.accounting.expense;


import com.helioplis.accounting.credit.Credit;
import com.helioplis.accounting.credit.CreditListDTO;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final ShiftRepo shiftRepo;
    private final  ExpenseMapper mapper;

    public Expense addNewExpense(Expense expense){
        Shift shift =  expense.getShift();
        System.out.println(shift.getId());
        shiftRepo.findById(shift.getId()).orElseThrow(() -> new ApiRequestException("No Shift with that Id"));
        System.out.println(shift.isClosed());
        if (shift.isClosed()){
            throw new ApiRequestException("This Shift is closed open it first and then modify");
        }
        return expenseRepo.save(expense);
    }

    List<ExpenseListDTO> listExpenses(String s_date, String e_date,Integer shiftId)
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
        List<Expense> expenses =  expenseRepo.findFilter(start_date,end_date,shiftId);

        return  expenses.stream().map(this :: convertToDTO).collect(Collectors.toList());

    }

    private ExpenseListDTO convertToDTO(Expense expense) {
        ExpenseListDTO dto = new ExpenseListDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setCreatedAt(expense.getCreatedAt());
        return dto;
    }

    public Expense updateExpense(ExpenseUpdateDTO dto, String username) {
        Expense myExpense = expenseRepo.findById(dto.getId()).orElseThrow(() -> new ApiRequestException("No Expense with that ID"));
        if (!myExpense.getUser().equals(username)){
            throw  new ApiRequestException("Only the user who created the expense can modify it");
        }
        if (dto.getShift() != null){
            Integer shiftId = dto.getShift().getId();
            Shift shift = shiftRepo.findById(shiftId).orElseThrow(()-> new ApiRequestException("No Shift by that ID"));
            if (shift.isClosed()){
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
    public void deleteExpense(Integer expenseId, Principal principal){
        Expense expense = expenseRepo.findById(expenseId).orElseThrow(()-> new ApiRequestException("No expense with that ID"));

        if (!expense.getUser().equals(principal.getName())){
            throw new ApiRequestException("Only the user who created the expense can delete it");
        }
        expenseRepo.delete(expense);
    }

}
