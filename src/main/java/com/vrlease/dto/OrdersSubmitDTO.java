package com.vrlease.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {
    private Long deviceId;
    //宿舍
    private String dormitory;
    //租赁时间,单位小时
    private Long leaseTime;
    //设备数量
    private Integer deviceAmount;
    //姓名
    private String userRealName;
    //付款方式
    private int payMethod;
    //备注
    private String remark;
    //电话
    private String phone;

    //配送状态  1立即送出  0选择具体时间
    private Integer deliveryStatus;

    //总金额
    private BigDecimal amount;
}
