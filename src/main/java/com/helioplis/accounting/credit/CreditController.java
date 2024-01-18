package com.helioplis.accounting.credit;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.order.ExcelHelper;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/credit/")
@AllArgsConstructor
@Slf4j
public class CreditController {
    @Autowired
    private final CreditService creditService;
    private final UserRepository userRepository;
    @PostMapping("add")
    public Credit addCredit(@RequestBody @Valid Credit credit,final BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            String errorMessage = String.join(", ", errors);
            throw new ApiRequestException(errorMessage);
        }
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        credit.setUser(user.get());
        return   creditService.addNewCredit(credit);
    }

    @GetMapping("list")
    public List<CreditListDTO> listCredits(
            @RequestParam(name = "start_date",required = false) String start_date,
            @RequestParam(name = "end_date",required = false) String  end_date,
            @RequestParam(name = "shift_id", required = false) Integer shiftId

    ){

        return creditService.listCredits(start_date,end_date, shiftId);
    }
    @PutMapping("update")
    public Credit updateCredit(@RequestBody  CreditUpdateDTO dto, Principal principal){
        return   creditService.updateCredit(dto, principal.getName());
    }
    @GetMapping("retrieve/{creditId}")
    public Credit retrieveCredit(@PathVariable Integer creditId, Principal principal){
        return creditService.retrieveCredit(creditId);
    }

    @DeleteMapping("delete/{creditId}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Integer creditId, Principal principal){
        creditService.deleteCredit(creditId, principal);
        return ResponseEntity.noContent().build();
    }
}
