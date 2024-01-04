package com.helioplis.accounting.shift;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import jakarta.validation.Valid;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/shift/")
@AllArgsConstructor
@Slf4j
public class ShiftController {
    private final ShiftService shiftService;
    private final UserRepository userRepository;
    @PostMapping("add")
    public Shift addShift(Principal principal){
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        Shift shift = new Shift();
        shift.setUserOpen(user.get());
        return shiftService.addNewShift(shift);
    }

    @GetMapping("list")
    public List<ShiftDTO> listShifts(Principal principal){
        return shiftService.shiftList();
    }

    @PostMapping("close")
    Integer closeShift(@RequestBody @Valid ShiftCloseRequestDTO shiftCloseRequestDTO, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            String errorMessage = String.join(", ", errors);
            throw new ApiRequestException(errorMessage);
        }
        return shiftService.closeShift(shiftCloseRequestDTO.getShiftId(), principal);
    }
}
