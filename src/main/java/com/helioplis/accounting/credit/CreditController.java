package com.helioplis.accounting.credit;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/v1/credit/")
@AllArgsConstructor
@Slf4j
public class CreditController {
    @Autowired
    private final CreditService creditService;
    private final UserRepository userRepository;
    @PostMapping("add")
    public void addCredit(@RequestBody @Valid Credit credit, Principal principal){
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        credit.setUser(user.get());
        creditService.addNewCredit(credit);
    }

    @GetMapping("list")
    public List<Credit> listCredits(
            @RequestParam(name = "start_date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start_date,
            @RequestParam(name = "end_date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end_date

    ){

        return creditService.listCredits(start_date,end_date);
    }
}
