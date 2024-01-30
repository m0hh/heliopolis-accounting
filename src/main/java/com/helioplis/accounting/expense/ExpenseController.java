package com.helioplis.accounting.expense;

import com.helioplis.accounting.credit.Credit;
import com.helioplis.accounting.credit.CreditUpdateDTO;
import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import com.helioplis.accounting.validator.DateConstraint;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/expense/")
@AllArgsConstructor
@Slf4j
public class ExpenseController {
    @Autowired
    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    @PostMapping("add")
    public Expense addExpense(@RequestBody @Valid Expense expense, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            String errorMessage = String.join(", ", errors);
            throw new ApiRequestException(errorMessage);
        }
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        expense.setUser(user.get());
        return expenseService.addNewExpense(expense);
    }

    @GetMapping("list")
    public List<Expense> listExpenses(
            @RequestParam(name = "start_date",required = false) String start_date,
            @RequestParam(name = "end_date",required = false) String end_date,
            @RequestParam(name = "shift_id", required = false) Integer shiftId,
            @RequestParam(name = "page", required = false) Integer page



    ){
        if (page == null){
            throw new ApiRequestException("Specify the page");
        }
        return expenseService.listExpenses(start_date,end_date,shiftId, page);
    }

    @PutMapping("update")
    public Expense updateExpense(@RequestBody ExpenseUpdateDTO dto, Principal principal){
        return   expenseService.updateExpense(dto, principal.getName());
    }

    @GetMapping("retrieve/{expenseId}")
    public Expense retrieveExpense(@PathVariable Integer expenseId, Principal principal){
        return expenseService.retrieveExpense(expenseId);
    }

    @DeleteMapping("delete/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer expenseId, Principal principal){
        expenseService.deleteExpense(expenseId, principal);
        return ResponseEntity.noContent().build();
    }




}
