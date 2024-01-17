package com.helioplis.accounting.firebase;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FireBaseTokenRequest {
    @NotBlank
    public String firebaseToken;
}
