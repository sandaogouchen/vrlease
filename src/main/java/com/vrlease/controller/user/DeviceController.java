package com.vrlease.controller.user;

import com.vrlease.dto.DeviceCommentDTO;
import com.vrlease.dto.DevicePageDTO;
import com.vrlease.dto.QueryBrandDTO;
import com.vrlease.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("userDeviceController")
@RequestMapping("/user/device")
@Slf4j
@Api("用户端租赁界面")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @PostMapping("/pageQuery")
    @ApiOperation("分页查询设备")
    public String pageQuery(DevicePageDTO devicePageDTO) {
        return deviceService.pageQuery(devicePageDTO);
    }

    @GetMapping("/query")
    @ApiOperation("查询设备")
    public String query(Long id) {
        return deviceService.query(id);
    }

    @PostMapping("/queryBybrand")
    @ApiOperation("根据品牌查询设备")
    public String queryBybrand(QueryBrandDTO queryBrandDTO) {
        return deviceService.queryBybrand(queryBrandDTO);
    }
}
