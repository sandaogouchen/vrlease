package com.vrlease.controller.user;

import cn.hutool.json.JSONUtil;
import com.sky.constant.JwtClaimsConstant;
import com.sky.utils.JwtUtil;
import com.vrlease.dto.UserDTO;
import com.vrlease.dto.UserLoginDTO;
import com.vrlease.dto.UserRegisterDTO;
import com.vrlease.entity.User;
import com.vrlease.properties.JwtProperties;
import com.vrlease.service.UserService;
import com.vrlease.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/code")
    public String sendCode(@RequestBody String phone, HttpSession session) {
        return userService.sendCode(phone, session);
    }

    @PostMapping("/login")
    public String login(UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        Map map = new HashMap();
        if(user != null) {
            map.put("success", userLoginVO);
        }
        else{
            map.put("-1","用户名或密码错误");
        }
        return JSONUtil.toJsonStr(map);
    }

    @PostMapping("/register")
    public String register(UserRegisterDTO userRegisterDTO){

        return userService.register(userRegisterDTO);
    }

    @PostMapping("/lookMyself")
    public String lookMyself(){
        return userService.lookMyself();
    }
}
