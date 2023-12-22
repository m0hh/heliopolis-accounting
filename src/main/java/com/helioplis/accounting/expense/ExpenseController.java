package com.helioplis.accounting.expense;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/v1/expense/")
@AllArgsConstructor
@Slf4j
public class ExpenseController {
    @Autowired
    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    @PostMapping("add")
    public void addExpense(@RequestBody @Valid Expense expense,Principal principal){
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        expense.setUser(user.get());
        expenseService.addNewExpense(expense);
    }

    @GetMapping("list")
    public List<Expense> listExpenses(
            @RequestParam(name = "start_date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start_date,
            @RequestParam(name = "end_date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end_date

    ){

        return expenseService.listExpenses(start_date,end_date);
    }
    @PostMapping("/getData")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> testAfterLogin(Principal p){
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" +p.getName());
    }



}
