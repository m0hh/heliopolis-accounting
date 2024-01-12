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
    public Shift addShift(@RequestBody(required = false) ShiftAddTimesDTO shiftAddTimesDTO,Principal principal){
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        Shift shift = new Shift();
        shift.setUserOpen(user.get());
        if (shiftAddTimesDTO != null) {
            if (shiftAddTimesDTO.getCreatedAt() != null) {
                shift.setCreatedAt(shiftAddTimesDTO.getCreatedAt());
            }
            if (shiftAddTimesDTO.getClosedAt() != null) {
                shift.setClosed_at(shiftAddTimesDTO.getClosedAt());
            }
        }
        return shiftService.addNewShift(shift);
    }

    @GetMapping("list")
    public List<ShiftDTO> listShifts(Principal principal){
        return shiftService.shiftList();
    }

    @PostMapping("close/{shiftId}")
    Integer closeShift(@PathVariable Integer shiftId, Principal principal){

        return shiftService.closeShift(shiftId, principal);
    }

    @PutMapping("reopen/{shiftId}")
    Shift reopenShift(@PathVariable Integer shiftId, Principal principal){

        return shiftService.reopen(shiftId, principal);
    }
}
