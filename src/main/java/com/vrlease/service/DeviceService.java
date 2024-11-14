package com.vrlease.service;

import com.vrlease.dto.DeviceAddDTO;
import com.vrlease.dto.DevicePageDTO;
import com.vrlease.dto.DeviceUpdateDTO;
import com.vrlease.dto.QueryBrandDTO;

public interface DeviceService {

    String addDevice(DeviceAddDTO deviceAddDTO);

    String updateDevice(DeviceUpdateDTO deviceUpdateDTO);

    String pageQuery(DevicePageDTO devicePageDTO);

    String query(Long id);

    String queryBybrand(QueryBrandDTO queryBrandDTO);
}
