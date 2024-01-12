package com.helioplis.accounting.shift;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShiftAddTimesDTO {
    private LocalDateTime createdAt;
    private LocalDateTime   closedAt;

}