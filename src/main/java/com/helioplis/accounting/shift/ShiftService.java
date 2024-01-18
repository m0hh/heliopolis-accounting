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
        if (shiftRepo.findOpenOrOverlappingShift(shift.getCreatedAt())){
            throw new ApiRequestException("There is an open shift or an overlapping shift, you need to close the open shift or modify the dates if overlapping");
        }
        return shiftRepo.save(shift);
    }

    public List<ShiftListDTO> shiftList(){
        List<Shift> shifts =  shiftRepo.findAll();
        return shifts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }
    private ShiftListDTO convertToDTO(Shift shift) {
        ShiftListDTO dto = new ShiftListDTO();
        dto.setId(shift.getId());
        dto.setUserOpen(shift.getUserOpen());
        dto.setTotalShift(shift.getTotalShift());
        dto.setCreatedAt(shift.getCreatedAt());
        dto.setClosed_at(shift.getClosed_at());
        dto.setClosed(shift.isClosed());
        return dto;
    }
    @Transactional
    public Integer closeShift(Integer shiftId, Principal principal){
        Shift shift = shiftRepo.findById(shiftId).orElseThrow(() -> new ApiRequestException("No shift is by that id"));
        if (shift.isClosed()){
            throw new ApiRequestException("This Shift is closed modify it first");
        }
        if (!shift.getUserOpen().equals(principal.getName())){

            throw new ApiRequestException("only the user who created the shift can close it");

        }
        return shiftRepo.closeShift(shift.getId());
    }

    public Shift reopen(Integer shidtId, Principal principal){
        if (shiftRepo.findOpenShift()){
            throw new ApiRequestException("There is an open shift you need to close that first");
        }
        Shift shift = shiftRepo.findById(shidtId).orElseThrow(()-> new ApiRequestException("There is no shift by that ID"));
        UserHelioplis user = userRepository.findByUsername(principal.getName()).get();
        if (!shift.getUserOpen().equals(user.getUsername())){
            throw new ApiRequestException("The user who opened the shift must reopen it");
        }
        shift.setClosed(false);
        return shiftRepo.save(shift);
    }

    public Shift retrieve(Integer shiftId){
        return shiftRepo.findById(shiftId).orElseThrow(() -> new ApiRequestException("No shift by that Id"));
    }


}
