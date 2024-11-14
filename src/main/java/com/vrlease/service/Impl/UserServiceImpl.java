package com.vrlease.service.Impl;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import com.vrlease.dto.UserLoginDTO;
import com.vrlease.dto.UserRegisterDTO;
import com.vrlease.entity.User;
import com.vrlease.mapper.UserMapper;
import com.vrlease.service.UserService;
import com.vrlease.util.BaseContext;
import com.vrlease.util.RandomUtil;
import com.vrlease.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.vrlease.util.RedisConstants.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserMapper userMapper;
    @Resource
    private  StringRedisTemplate stringRedisTemplate1;

    public String sendCode(String phone, HttpSession session){
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return "-1";
        }

        String lastTimeKey = LOGIN_CODE_LAST_TIME + phone;
        String lastTime = stringRedisTemplate.opsForValue().get(lastTimeKey);
        if (lastTime != null) {
            long time = System.currentTimeMillis() - Long.parseLong(lastTime);
//            if (time < CODE_INTERVAL_TIME * 1000) {TODO
//                return "请" + (CODE_INTERVAL_TIME - time/1000) + "秒后重试";
//            }
        }

        String code = RandomUtil.getSixBitRandom();

        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(lastTimeKey, String.valueOf(System.currentTimeMillis()), CODE_INTERVAL_TIME, TimeUnit.SECONDS);

        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId("")//TODO 阿里云短信
                // 您的AccessKey Secret
                .setAccessKeySecret("");//TODO

        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = null;
        Map map = new HashMap();
        map.put("code",code);
        try {
            client = new Client(config);
            SendSmsRequest request = new SendSmsRequest();

            request.setSignName("阿里云短信测试");//签名名称
            request.setTemplateCode("SMS_154950000");
            request.setPhoneNumbers(phone);
            //这里的参数是json格式的字符串
            request.setTemplateParam(JSONObject.toJSONString(map));
            SendSmsResponse response = client.sendSms(request);
            System.out.println("发送成功："+new Gson().toJson(response));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送验证码失败", e);
            return "-1";
        }


    }

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        Map map = new HashMap();
        User user = new User();
        String username = userLoginDTO.getUsername();
        if(username != null){
            user = userMapper.findUserByName(username);
        }
        else if(userLoginDTO.getMail() != null){
            user = userMapper.findUserBymail(userLoginDTO.getMail());
        }
        else if(userLoginDTO.getPhone() != null){
            user = userMapper.findUserByPhone(userLoginDTO.getPhone());
        }
        if(user == null){
            RuntimeException e = new RuntimeException("用户名不存在");
        }
        String password = userLoginDTO.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(md5Password)){
            RuntimeException e = new RuntimeException("密码错误");
        }
        return user;
    }

    @Override
    public String register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUserName();
        String password = userRegisterDTO.getPassword();
        String mail = userRegisterDTO.getMail();
        String password2 = userRegisterDTO.getPassword2();
        String icon = userRegisterDTO.getIcon();
        Map map = new HashMap();
        if(username.isBlank()){
            String s = "用户名不能为空";
            map.put("-1",s);
            return JSONUtil.toJsonStr(map);
        }
        if(mail.isBlank()){
            String s = "邮箱不能为空";
            map.put("-1",s);
            return JSONUtil.toJsonStr(map);
        }
        if(password.isBlank()){
            String s = "密码不能为空";
            map.put("-1",s);
            return JSONUtil.toJsonStr(map);
        }
        if(!password.equals(password2)){
            String s = "两次密码不一致";
            map.put("-1",s);
            return JSONUtil.toJsonStr(map);
        }
        //username和邮箱不能重复
        User user1 = userMapper.findUserByName(username);
        if(user1 != null){
            String s = "用户名已存在";
            map.put("-1",s);
            return JSONUtil.toJsonStr(map);
        }
        User user2 = userMapper.findUserBymail(mail);
        if(user2 != null){
            String s = "邮箱已存在";
            map.put("-1",s);
            return JSONUtil.toJsonStr(map);
        }
        User user = new User();

        user.setUsername(username);
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(md5);
        user.setIcon(icon);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setMail(mail);
        userMapper.insert(user);
        String s = "注册成功";
        map.put("success",s);
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public String lookMyself() {
        Long id = BaseContext.getCurrentId();
        String str = stringRedisTemplate.opsForValue().get(USER_KEY + id);
        Map map = new HashMap();
        if(str == null){
            User user = userMapper.getById(id);
            stringRedisTemplate.opsForValue().set(USER_KEY + id, JSONUtil.toJsonStr(user));
            map.put("user",user);
        }
        else{
            map.put("user",str);
        }
        return JSONUtil.toJsonStr(map);

    }


//


}
