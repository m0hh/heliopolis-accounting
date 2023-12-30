package com.helioplis.accounting.shift;

import com.helioplis.accounting.exeption.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ShiftService {
    private final ShiftRepo shiftRepo;

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
}
