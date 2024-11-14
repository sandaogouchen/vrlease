package com.vrlease.mapper;
import com.vrlease.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumber(ShoppingCart shoppingCart);

    void insert(ShoppingCart shoppingCart);

    void deleteByUserId(Long userId);
}
