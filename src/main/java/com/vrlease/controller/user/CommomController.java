package com.vrlease.controller.user;

import cn.hutool.json.JSONUtil;
import com.vrlease.util.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.jayway.jsonpath.internal.Utils.concat;


@RestController("userCommemController")
@RequestMapping("/user/commom")
@Slf4j
@Api("通用接口")
public class CommomController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public String upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            String filename = file.getOriginalFilename();
            String suffix = filename.substring(filename.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            String filepath = aliOssUtil.upload(file.getBytes(), concat(uuid, suffix));
            Map map = new HashMap();
            map.put("filepath",filepath);
            return JSONUtil.toJsonStr(map);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
            throw new RuntimeException(e);
        }
    }
}
