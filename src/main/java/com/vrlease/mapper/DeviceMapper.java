package com.vrlease.mapper;

import com.vrlease.entity.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceMapper {
    void insert(Device device);

    void updateById(Device device, Long id);

    Device selectById(Long id);

    List<Device> pageQuery(Integer page, Integer size);

    List<Device> queryBybrand(Long brandId);
}
