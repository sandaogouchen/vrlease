package com.vrlease.mapper;

import com.vrlease.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User findUserByName(String username);

    @Select("select * from user where phone = #{phone}")
    User findUserByPhone(String phone);


    void insertByPhone(User user);

    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    User findUserBymail(String mail);
}
