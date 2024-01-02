package com.helioplis.accounting.credit;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.expense.ExpenseRepo;
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
public class CreditService {
    private final CreditRepo creditRepo;
    private final ShiftRepo shiftRepo;

    public Credit addNewCredit(Credit credit){
        Shift shift =  credit.getShift();
        shiftRepo.findById(shift.getId()).orElseThrow(() -> new ApiRequestException("No Shift with that Id"));
        return creditRepo.save(credit);
    }

    List<Credit> listCredits(String s_date, String e_date)
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
        return creditRepo.findDateBetween(start_date,end_date);

    }
}
