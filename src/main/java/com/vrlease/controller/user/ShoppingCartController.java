package com.vrlease.controller.user;

import com.vrlease.dto.ShoppingCartDTO;
import com.vrlease.entity.ShoppingCart;
import com.vrlease.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
@Api("购物车")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("添加购物车, 根本没有购物车...")
    public String add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        return shoppingCartService.add(shoppingCartDTO);

    }

    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public String list() {
        return shoppingCartService.showCart();
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public String clean() {
        return shoppingCartService.clean();
    }
}
