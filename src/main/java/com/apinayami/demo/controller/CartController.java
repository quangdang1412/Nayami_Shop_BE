package com.apinayami.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.Impl.CartItemServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.apinayami.demo.model.UserModel;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth") 
public class CartController {

    private final CartItemServiceImpl cartItemService;
    private final JwtConfig jwtConfig;

    private String extractUserEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7); 
        Jwt decodedToken = jwtConfig.decodeToken(token);
        return decodedToken.getSubject(); 
    }

    @GetMapping
    public ResponseData<List<CartItemDTO>> getCart(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
        }

        List<CartItemDTO> cart = cartItemService.getUserCart(email);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy giỏ hàng thành công", cart);
    }

    @PostMapping
    public ResponseData<CartItemDTO> addToCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CartItemDTO request) {

        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để thêm vào giỏ hàng");
        }

        CartItemDTO addedItem = cartItemService.addToCart(email, request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm vào giỏ hàng thành công", addedItem);
    }

    @PutMapping("/{id}")
    public ResponseData<CartItemDTO> updateCartItem(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @PathVariable Long id,
        @RequestBody CartItemDTO request) {
        
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để cập nhật giỏ hàng");
        }

        CartItemDTO updatedItem = cartItemService.updateCartItem(email, id, request.getQuantity());
        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật giỏ hàng thành công", updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> removeFromCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {
        
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xóa sản phẩm khỏi giỏ hàng");
        }

        cartItemService.removeFromCart(email, id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Xóa sản phẩm khỏi giỏ hàng thành công");
    }

    @DeleteMapping
    public ResponseData<Void> clearCart(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xóa giỏ hàng");
        }

        cartItemService.clearCart(email);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Đã xóa toàn bộ giỏ hàng");
    }
}
