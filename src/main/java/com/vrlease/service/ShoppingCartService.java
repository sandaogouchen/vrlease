package com.vrlease.service;

import com.vrlease.dto.ShoppingCartDTO;

public interface ShoppingCartService {
    String add(ShoppingCartDTO shoppingCartDTO);

    String showCart();

    String clean();
}
