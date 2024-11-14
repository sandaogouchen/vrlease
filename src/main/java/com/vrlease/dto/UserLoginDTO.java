package com.vrlease.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String phone;
    private String code;
    private String username;
    private String mail;
    private String password;
}
