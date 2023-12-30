package com.helioplis.accounting.shift;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
}
