package com.vrlease.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String userName;
    private String password;
    private String password2;
    private String phone;
    private String icon;
    private String mail;
}
