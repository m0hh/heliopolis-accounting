package com.helioplis.accounting.shift;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ShiftService {
    private final ShiftRepo shiftRepo;
    private final UserRepository userRepository;

    public Shift addNewShift(Shift shift){
        if (shiftRepo.findOpenShift()){
            throw new ApiRequestException("There is an open shift you need to close that first");
        }
        return shiftRepo.save(shift);
    }

    public List<ShiftDTO> shiftList(){
        List<Shift> shifts =  shiftRepo.findAll();
        return shifts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }
    private ShiftDTO convertToDTO(Shift shift) {
        ShiftDTO dto = new ShiftDTO();
        dto.setId(shift.getId());
        dto.setUserOpen(shift.getUserOpen());
        dto.setUserClose(shift.getUserClose());
        dto.setTotalShift(shift.getTotalShift());
        dto.setCreatedAt(shift.getCreatedAt());
        dto.setClosed_at(shift.getClosed_at());
        return dto;
    }
    @Transactional
    public Integer closeShift(Integer shiftId, Principal principal){
        Shift shift = shiftRepo.findById(shiftId).orElseThrow(() -> new ApiRequestException("No shift is by that id"));
        if (shift.getClosed_at() != null){
            throw new ApiRequestException("This Shift is closed modify it first");
        }
        UserHelioplis user = userRepository.findByUsername(principal.getName()).get();
        return shiftRepo.closeShift(shift.getId(), user.getId());
    }

    public Shift reopen(Integer shidtId, Principal principal){
        if (shiftRepo.findOpenShift()){
            throw new ApiRequestException("There is an open shift you need to close that first");
        }
        Shift shift = shiftRepo.findById(shidtId).orElseThrow(()-> new ApiRequestException("There is no shift by that ID"));
        UserHelioplis user = userRepository.findByUsername(principal.getName()).get();
        if (shift.getUserOpen().getId() != user.getId()){
            throw new ApiRequestException("The user who opened the shift must reopen it");
        }
        shift.setClosed_at(null);
        return shiftRepo.save(shift);
    }
}
