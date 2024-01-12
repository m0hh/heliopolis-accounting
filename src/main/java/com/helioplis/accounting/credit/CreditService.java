package com.helioplis.accounting.credit;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.expense.ExpenseRepo;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@AllArgsConstructor
@Service
public class CreditService {
    private final CreditRepo creditRepo;
    private final ShiftRepo shiftRepo;
    private final CreditMapper mapper;

    public Credit addNewCredit(Credit credit){

        Shift shift = shiftRepo.findById(credit.getShift().getId()).orElseThrow(() -> new ApiRequestException("No Shift with that Id"));
        if (shift.getClosed_at() != null){
            throw new ApiRequestException("This Shift is closed open it first and then modify");
        }
        return creditRepo.save(credit);
    }

    List<Credit> listCredits(String s_date, String e_date, Integer shiftId)
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
        return creditRepo.findFilter(start_date,end_date, shiftId);

    }

    public Credit updateCredit(CreditUpdateDTO dto, Integer userId) {
        Credit myCredit = creditRepo.findById(dto.getId()).orElseThrow(() -> new ApiRequestException("No Credit with that ID"));
        if (myCredit.getUser().getId() != userId){
            throw  new ApiRequestException("Only the user who created the credit can modify it");
        }
        if (dto.getShift() != null){
            Integer shiftId = dto.getShift().getId();
            Shift shift = shiftRepo.findById(shiftId).orElseThrow(()-> new ApiRequestException("No Shift by that ID"));
            if (shift.getClosed_at() != null){
                throw new ApiRequestException("This Shift is closed open it first and then modify");
            }

        }
        mapper.updateCreditFromDto(dto, myCredit);

        return creditRepo.save(myCredit);
    }

    public Credit retrieveCredit(Integer creditId){
        Credit credit = creditRepo.findById(creditId).orElseThrow(()-> new ApiRequestException("No Credit with that ID"));
        return credit;
    }

    @Transactional
    public void deleteCredit(Integer creditId, Principal principal){

        Credit credit = creditRepo.findById(creditId).orElseThrow(()-> new ApiRequestException("No credit with that ID"));

        if (!credit.getUser().getUsername().equals(principal.getName())){
            throw new ApiRequestException("Only the user who created the credit can delete it");
        }
        creditRepo.delete(credit);
    }
}
