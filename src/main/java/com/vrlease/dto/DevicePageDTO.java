package com.vrlease.dto;

import lombok.Data;

@Data
public class DevicePageDTO {
    private Integer page;//当前第几页
    private Integer size;//每一页的数量
}
