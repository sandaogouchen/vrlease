package com.vrlease.dto;

import lombok.Data;

@Data
public class DeviceUpdateDTO {
    private Long id;
    private String name;
    private String brandId;
    private String images;
    private Integer sold;
}
