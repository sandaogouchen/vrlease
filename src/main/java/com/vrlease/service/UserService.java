package com.vrlease.service;

import com.vrlease.dto.UserLoginDTO;
import com.vrlease.dto.UserRegisterDTO;
import com.vrlease.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    String sendCode(String phone, HttpSession session);

    User login(UserLoginDTO userLoginDTO);

    String register(UserRegisterDTO userRegisterDTO);

    String lookMyself();
}
