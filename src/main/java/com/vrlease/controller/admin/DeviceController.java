package com.vrlease.controller.admin;

import com.vrlease.dto.DeviceAddDTO;
import com.vrlease.dto.DevicePageDTO;
import com.vrlease.dto.DeviceUpdateDTO;
import com.vrlease.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("adminDeviceController")
@RequestMapping("/admin/device")
@Api("后台管理商品设备")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @PostMapping("/adddevice")
    @ApiOperation("添加设备")
    public String addDevice(DeviceAddDTO deviceAddDTO){
        return deviceService.addDevice(deviceAddDTO);
    }

    @PostMapping("/updatedevice")
    @ApiOperation("修改设备")
    public String updateDevice(DeviceUpdateDTO deviceUpdateDTO){
        return deviceService.updateDevice(deviceUpdateDTO);
    }

    @PostMapping("/pageQuery")
    @ApiOperation("分页查询设备")
    public String pageQuery(DevicePageDTO devicePageDTO){
        return deviceService.pageQuery(devicePageDTO);
    }

    @GetMapping("/query")
    @ApiOperation("查询设备")
    public String query(Long id){
        return deviceService.query(id);
    }

}
