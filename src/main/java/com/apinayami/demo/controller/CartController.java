package com.apinayami.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.Impl.CartItemServiceImpl;
import com.apinayami.demo.model.UserModel;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemServiceImpl cartItemService;

    @GetMapping
    public ResponseData<List<CartItemDTO>> getCart(@AuthenticationPrincipal UserModel user) {
        List<CartItemDTO> cart = cartItemService.getUserCart(user.getEmail());
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy giỏ hàng thành công", cart);
    }

    @PostMapping
    public ResponseData<CartItemDTO> addToCart(
            @AuthenticationPrincipal UserModel user,
            @RequestBody CartItemDTO request) {
        CartItemDTO addedItem = cartItemService.addToCart(user.getEmail(), request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm vào giỏ hàng thành công", addedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> removeFromCart(
            @AuthenticationPrincipal UserModel user,
            @PathVariable Long id) {
        cartItemService.removeFromCart(user.getEmail(), id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Xóa sản phẩm khỏi giỏ hàng thành công");
    }

    @DeleteMapping
    public ResponseData<Void> clearCart(@AuthenticationPrincipal UserModel user) {
        cartItemService.clearCart(user.getEmail());
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Đã xóa toàn bộ giỏ hàng");
    }
}
