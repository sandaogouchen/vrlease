package com.vrlease.controller.user;

import cn.hutool.json.JSONUtil;
import com.vrlease.dto.OrdersPaymentDTO;
import com.vrlease.dto.OrdersSubmitDTO;
import com.vrlease.dto.OrdershistoryDTO;
import com.vrlease.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/orders")
@Api("下单")
@Slf4j
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    @ApiOperation("下单")
    public String submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
       return ordersService.submit(ordersSubmitDTO);
    }

    @PostMapping("/payment")
    @ApiOperation("订单支付")
    public String payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        Map map = new HashMap();
        String OrderPayment = ordersService.payment(ordersPaymentDTO);
        map.put("OrderPayment",OrderPayment);
        return JSONUtil.toJsonStr(map);
    }


    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public String page(@RequestBody OrdershistoryDTO ordershistoryDTO) {
        return ordersService.pageQuery4User(ordershistoryDTO);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public String details(@PathVariable("id") Long id) {
        return ordersService.details(id);

    }

    /**
     * 用户取消订单
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public void cancel(@PathVariable("id") Long id) throws Exception {
        ordersService.userCancelById(id);
    }




}
