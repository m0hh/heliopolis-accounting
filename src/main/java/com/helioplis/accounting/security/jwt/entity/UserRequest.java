package com.helioplis.accounting.security.jwt.entity;

import lombok.Data;

@Data
public class UserRequest {

    private String username;
    private String password;
}