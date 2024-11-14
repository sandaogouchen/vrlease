package com.vrlease.mapper;

import com.vrlease.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);

    List<OrderDetail> list(Long id);

    List<OrderDetail> getByOrderId(Long id);
}
