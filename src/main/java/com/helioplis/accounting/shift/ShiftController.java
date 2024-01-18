package com.helioplis.accounting.shift;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public Shift addShift(@RequestBody(required = false) ShiftAddTimesDTO shiftAddTimesDTO,Principal principal){
        Optional<UserHelioplis> user = userRepository.findByUsername(principal.getName());
        Shift shift = new Shift();
        shift.setUserOpen(user.get());
        if (shiftAddTimesDTO != null) {
            if (shiftAddTimesDTO.getCreatedAt() != null) {
                shift.setCreatedAt(shiftAddTimesDTO.getCreatedAt());
            }
            if (shiftAddTimesDTO.getClosedAt() != null) {
                System.out.println(shiftAddTimesDTO.getClosedAt());
                shift.setClosed_at(shiftAddTimesDTO.getClosedAt());
                shift.setClosed(true);
            }
        }
        return shiftService.addNewShift(shift);
    }

    @GetMapping("list")
    public List<ShiftListDTO> listShifts(Principal principal,
                                         @RequestParam(name = "page", required = false) Integer page,
                                         @RequestParam(name = "start_date",required = false) String start_date,
                                         @RequestParam(name = "end_date",required = false) String end_date,
                                         @RequestParam(name = "get_user", required = false,defaultValue = "false") String get_user)
    {
        if (page == null){
            throw new ApiRequestException("Specify the page");
        }
        Principal getUser = null;
        if (!get_user.equals("false")){
            getUser = principal;
        }
        return shiftService.shiftList(start_date, end_date, getUser, page);
    }

    @PostMapping("close/{shiftId}")
    Integer closeShift(@PathVariable Integer shiftId, Principal principal){

        return shiftService.closeShift(shiftId, principal);
    }

    @PutMapping("reopen/{shiftId}")
    Shift reopenShift(@PathVariable Integer shiftId, Principal principal){

        return shiftService.reopen(shiftId, principal);
    }

    @GetMapping("retrieve/{shiftId}")
    Shift retrieveShift(@PathVariable Integer shiftId, Principal principal){
        return shiftService.retrieve(shiftId);
    }


}
