package com.vrlease.service;

import com.vrlease.dto.*;

public interface OrdersService {

    String submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    String payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户端订单分页查询

     * @return
     */
    String pageQuery4User(OrdershistoryDTO ordershistoryDTO);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    String details(Long id);

    /**
     * 用户取消订单
     *
     * @param id
     */
    void userCancelById(Long id) throws Exception;



    /**
     * 条件搜索订单
     * @param ordersPageQueryDTO
     * @return
     */
    String conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

//    /**
//     * 各个状态的订单数量统计
//     * @return
//     */
//    String statistics();

    /**
     * 接单
     *
     *TODO 管理端
//    void confirm(OrdersConfirmDTO ordersConfirmDTO);

//    /**
//     * 拒单
//     *
//     * @param ordersRejectionDTO
//     */
//    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;


    /**
     * 完成订单
     *,,
     * @param id
     */
    void complete(Long id);
}
