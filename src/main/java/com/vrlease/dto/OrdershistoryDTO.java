package com.vrlease.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class OrdershistoryDTO {
    private Integer page;
    private Integer pageSize;
}
