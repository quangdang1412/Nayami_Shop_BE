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
        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
        }
        List<CartItemDTO> cart = cartItemService.getUserCart(user.getEmail());
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy giỏ hàng thành công", cart);
    }

    @PostMapping
    public ResponseData<CartItemDTO> addToCart(
            @AuthenticationPrincipal UserModel user,
            @RequestBody CartItemDTO request) {

        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để thêm vào giỏ hàng");
        }        
        CartItemDTO addedItem = cartItemService.addToCart(user.getEmail(), request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm vào giỏ hàng thành công", addedItem);
    }
    @PutMapping("/{id}")
    public ResponseData<CartItemDTO> updateCartItem(
        @AuthenticationPrincipal UserModel user,
        @PathVariable Long id,
        @RequestBody CartItemDTO request) {
        
        if (user == null) {
        return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để cập nhật giỏ hàng");
        }
        
        CartItemDTO updatedItem = cartItemService.updateCartItem(user.getEmail(), id, request.getQuantity());
        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật giỏ hàng thành công", updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> removeFromCart(
            @AuthenticationPrincipal UserModel user,
            @PathVariable Long id) {
        if (user == null) {
                return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để thêm vào giỏ hàng");
        }         
        cartItemService.removeFromCart(user.getEmail(), id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Xóa sản phẩm khỏi giỏ hàng thành công");
    }

    @DeleteMapping
    public ResponseData<Void> clearCart(@AuthenticationPrincipal UserModel user) {
        if (user == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để thêm vào giỏ hàng");
        } 
        cartItemService.clearCart(user.getEmail());
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Đã xóa toàn bộ giỏ hàng");
    }
}
