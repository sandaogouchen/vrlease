package com.vrlease.util;

import com.vrlease.dto.UserDTO;

public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO userDTO){
        tl.set(userDTO);
    }

    public static UserDTO getUserDTO(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
