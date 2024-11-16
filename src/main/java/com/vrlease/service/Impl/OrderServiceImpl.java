package com.vrlease.service.Impl;



import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vrlease.dto.*;
import com.vrlease.entity.*;
import com.vrlease.mapper.*;
import com.vrlease.service.OrdersService;
import com.vrlease.util.BaseContext;
import com.vrlease.util.WeChatPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vrlease.entity.Orders.PENDING_PAYMENT;
import static com.vrlease.entity.Orders.UN_PAID;
import static java.lang.Math.min;

@Service
@Slf4j
public class OrderServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
//    @Autowired
//    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
//    @Autowired
//    private WebSocketServer webSocketServer;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public String submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 创建新订单对象
        Orders orders = Orders.builder()
                .deviceId(ordersSubmitDTO.getDeviceId())
                .deviceAccount(ordersSubmitDTO.getDeviceAmount())
                .leaseTime(ordersSubmitDTO.getLeaseTime())
                .userRealName(ordersSubmitDTO.getUserRealName())
                .payMethod(ordersSubmitDTO.getPayMethod())
                .remark(ordersSubmitDTO.getRemark())
                .phone(ordersSubmitDTO.getPhone())
                .deliveryStatus(ordersSubmitDTO.getDeliveryStatus())
                .amount(ordersSubmitDTO.getAmount())
                // 设置基础订单信息
                .userId(BaseContext.getCurrentId())
                .status(PENDING_PAYMENT)
                .payStatus(UN_PAID)
                .orderTime(LocalDateTime.now())
                .orderId(String.valueOf(System.currentTimeMillis())) // 生成订单号
                .build();

        // 插入订单
        Long id = nextId("order");
        orders.setOrderId(String.valueOf(id));
        ordersMapper.insert(orders);
        // 由于使用了 @Options(useGeneratedKeys = true)，orders 对象现在包含了生成的 ID
        // 创建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);

        return JSONUtil.toJsonStr(result);
    }
    private static final long BEGIN_TIMESTAMP = 1731182520L;
    public long nextId(String keyPrefix){
        LocalDateTime now = LocalDateTime.now();
        Long second = now.toEpochSecond(ZoneOffset.UTC);

        Long offset = second - BEGIN_TIMESTAMP;
        String day = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long cnt = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + day);
        return offset << 32 | cnt;
    }





    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
public String payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
    // 1. 获取当前用户
    Long userId = BaseContext.getCurrentId();
    User user = userMapper.getById(userId);
    if(user == null) {
        throw new RuntimeException("用户不存在");
    }

    // 2. 根据订单号查询订单
    Orders ordersDB = ordersMapper.getByNumberAndUserId(ordersPaymentDTO.getOrderNumber(), userId);
    if(ordersDB == null) {
        throw new RuntimeException("订单不存在");
    }

    // 3. 判断订单状态
    if(ordersDB.getStatus() > PENDING_PAYMENT) {
        throw new RuntimeException("订单状态异常");
    }

    // 4. 调用微信支付接口
    JSONObject result = weChatPayUtil.pay(
            ordersPaymentDTO.getOrderNumber(), // 商户订单号
            ordersDB.getAmount(), // 实际支付金额
            "VR设备租赁订单-" + ordersDB.getOrderId(), // 商品描述
            user.getOpenid() // 微信用户openid
    );

    // 5. 判断支付结果
    if(result.getString("code") != null) {
        if(result.getString("code").equals("ORDERPAID")) {
            throw new RuntimeException("该订单已支付");
        }
        throw new RuntimeException("支付失败:" + result.getString("message")); 
    }

    // 6. 返回支付所需参数
    Map<String, Object> map = new HashMap<>();
    map.put("timeStamp", result.getString("timeStamp"));
    map.put("nonceStr", result.getString("nonceStr")); 
    map.put("package", result.getString("package"));
    map.put("signType", result.getString("signType"));
    map.put("paySign", result.getString("paySign"));

    return JSONUtil.toJsonStr(map);
}

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = ordersMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);

//        //通过websocket向客户端浏览器推送消息 type orderId content
//        Map map = new HashMap();
//        map.put("type",1); // 1表示来单提醒 2表示客户催单
//        map.put("orderId",ordersDB.getId());
//        map.put("content","订单号：" + outTradeNo);
//
//        String json = JSON.toJSONString(map);
//        webSocketServer.sendToAllClient(json);
    }

    /**
     * 用户端订单分页查询
     *
     *
     * @return
     */
    public String pageQuery4User(OrdershistoryDTO ordershistoryDTO) {
        Integer pageNum = ordershistoryDTO.getPage();
        Integer pageSize = ordershistoryDTO.getPageSize();
        Long id = BaseContext.getCurrentId();
        List<Orders> orders = ordersMapper.pageQuery1(id);
        Integer pos = min(pageNum * pageSize, orders.size());
        List<Orders> orderList = new ArrayList<>();
        Map map = new HashMap();
        for (Integer i = (pageNum - 1) * pageSize; i < pos; i++) {
            Orders orders1 = new Orders();
            BeanUtils.copyProperties(orders.get(i), orders1);
            orderList.add(orders1);
            map.put("i", orders1);
        }
        return JSONUtil.toJsonStr(map);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    public String details(Long id) {
        // 根据Number查询订单
        Orders orders = ordersMapper.getByOrderId(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装并返回
        Map map = new HashMap();
        map.put("order", orders);
        map.put("orderDetailList", orderDetailList);

        return JSONUtil.toJsonStr(map);
    }

//     用户取消订单
//     *
//     * @param id
//     *
    public void userCancelById(Long id) throws Exception {
        // 根据id查询订单
        Orders ordersDB = ordersMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new RuntimeException("订单不存在");
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new RuntimeException("订单不能取消");
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
            weChatPayUtil.refund(
                    ordersDB.getOrderId(), //商户订单号
                    ordersDB.getOrderId(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        ordersMapper.update(orders);
    }
    


    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public String conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        Page<Orders> page = ordersMapper.pageQuery(ordersPageQueryDTO);

        List<Map<String, Object>> ordersList = new ArrayList<>();

        for (Orders order : page.getRecords()) {
            Map<String, Object> orderMap = new HashMap<>();
            // 复制订单基本信息
            BeanUtils.copyProperties(order, orderMap);

            // 获取订单菜品信息字符串
            String orderDishes = getOrderDishesStr(order);
            orderMap.put("orderDishes", orderDishes);

            // 获取订单详情列表
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(order.getId());
            orderMap.put("orderDetailList", orderDetailList);

            ordersList.add(orderMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("records", ordersList);

        return JSONUtil.toJsonStr(result);
    }

    private String getOrderVOList(Page<Orders> page) {

        List<Map<String, Object>> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getRecords();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                Map<String, Object> orderMap = new HashMap<>();
                // 复制订单基本信息
                BeanUtils.copyProperties(orders, orderMap);

                // 获取订单菜品信息
                String orderDishes = getOrderDishesStr(orders);
                orderMap.put("orderDishes", orderDishes);

                // 获取订单详情
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
                orderMap.put("orderDetailList", orderDetailList);

                orderVOList.add(orderMap);
            }
        }

        return JSONUtil.toJsonStr(orderVOList);
    }

    /**
     * 根据订单id获取菜品信息字符串
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getOrderId() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    //TODO
//    public OrderStatisticsVO statistics() {
//        // 根据状态，分别查询出待接单、待派送、派送中的订单数量
//        Integer toBeConfirmed = ordersMapper.countStatus(Orders.TO_BE_CONFIRMED);
//        Integer confirmed = ordersMapper.countStatus(Orders.CONFIRMED);
//        Integer deliveryInProgress = ordersMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
//
//        // 将查询出的数据封装到orderStatisticsVO中响应
//        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
//        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
//        orderStatisticsVO.setConfirmed(confirmed);
//        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
//        return orderStatisticsVO;
//    }

//    /**
//     * 接单
//     *
//     * @param ordersConfirmDTO
//     */
//    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
//        Orders orders = Orders.builder()
//                .id(ordersConfirmDTO.getId())
//                .status(Orders.CONFIRMED)
//                .build();
//
//        ordersMapper.update(orders);
//    }

//    /**
//     * 拒单
//     *
//     * @param ordersRejectionDTO
//     */
//    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
//        // 根据id查询订单
//        Orders ordersDB = ordersMapper.getById(ordersRejectionDTO.getId());
//
//        // 订单只有存在且状态为2（待接单）才可以拒单
//        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
//            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
//        }
//
//        //支付状态
//        Integer payStatus = ordersDB.getPayStatus();
//        if (payStatus == Orders.PAID) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
//            log.info("申请退款：{}", refund);
//        }
//
//        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
//        Orders orders = new Orders();
//        orders.setId(ordersDB.getId());
//        orders.setStatus(Orders.CANCELLED);
//        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
//        orders.setCancelTime(LocalDateTime.now());
//
//        ordersMapper.update(orders);
//    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 根据id查询订单
        Orders ordersDB = ordersMapper.getById(ordersCancelDTO.getId());

        if(ordersDB == null){
            throw new RuntimeException("订单不存在");
        }

        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    ordersDB.getOrderId(),
                    ordersDB.getOrderId(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // 管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        ordersMapper.update(orders);
    }

//    /**
//     * 派送订单
//     *
//     * @param id
//     */
//    public void delivery(Long id) {
//        // 根据id查询订单
//        Orders ordersDB = ordersMapper.getById(id);
//
//        // 校验订单是否存在，并且状态为3
//        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
//            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
//        }
//
//        Orders orders = new Orders();
//        orders.setId(ordersDB.getId());
//        // 更新订单状态,状态转为派送中
//        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
//
//        ordersMapper.update(orders);
//    }

    /**
     * 完成订单
     *
     * @param id
     */
    public void complete(Long id) {
        // 根据id查询订单
        Orders ordersDB = ordersMapper.getById(id);

        // 校验订单是否存在，并且状态为4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new RuntimeException("订单状态异常");
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);

        ordersMapper.update(orders);
    }

}
