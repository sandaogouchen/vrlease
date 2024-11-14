package com.vrlease.service.Impl;

import cn.hutool.json.JSONUtil;
import com.sky.context.BaseContext;
import com.vrlease.dto.ShoppingCartDTO;
import com.vrlease.entity.ShoppingCart;
import com.vrlease.mapper.ShoppingCartMapper;
import com.vrlease.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public String add(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list  = shoppingCartMapper.list(shoppingCart);
        if(list != null && list.size() > 0){
            ShoppingCart cartServiceOne = list.get(0);
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number);
            shoppingCartMapper.updateNumber(cartServiceOne);
        }else{
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
        Map map = new HashMap();
        map.put("success","添加成功");
        return JSONUtil.toJsonStr(map);
    }

    @Override
    public String showCart() {
        return "";
    }

    @Override
    public String clean() {
        return "";
    }
}
