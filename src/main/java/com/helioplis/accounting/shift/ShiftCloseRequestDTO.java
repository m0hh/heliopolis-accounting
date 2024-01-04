package com.helioplis.accounting.shift;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiftCloseRequestDTO {
    @NotNull
    private Integer shiftId;

}