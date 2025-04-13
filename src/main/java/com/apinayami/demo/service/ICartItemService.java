package com.apinayami.demo.service;

import java.util.List;

import com.apinayami.demo.dto.request.CartItemDTO;

public interface ICartItemService {
    List<CartItemDTO> getUserCart(String email);

    CartItemDTO addToCart(String email, CartItemDTO request);

    CartItemDTO updateCartItem(String email, Long cartItemId, Integer request);

    void removeFromCart(String email, Long cartItemId);

    
}
