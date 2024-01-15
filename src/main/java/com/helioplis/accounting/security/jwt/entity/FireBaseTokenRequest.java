package com.helioplis.accounting.security.jwt.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FireBaseTokenRequest {
    @NotBlank
    public String firebaseToken;
}
