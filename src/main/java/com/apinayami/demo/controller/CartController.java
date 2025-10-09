package com.apinayami.demo.controller;

import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.service.ICartItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final ICartItemService cartItemService;
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
    public ResponseData<?> getCart(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String email = extractUserEmail(authHeader);
            if (email == null) {
                return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để xem giỏ hàng");
            }

            List<CartItemDTO> cartItems = cartItemService.getUserCart(email);
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy giỏ hàng thành công", cartItems);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi lấy giỏ hàng", e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<?> addToCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CartItemDTO request) {
        try {
            String email = extractUserEmail(authHeader);
            if (email == null) {
                return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để thêm vào giỏ hàng");
            }

            CartItemDTO addedItem = cartItemService.addToCart(email, request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm vào giỏ hàng thành công", addedItem);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi thêm vào giỏ hàng",
                    e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<?> updateCartItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable @NotNull @Positive Long id,
            @Valid @RequestBody CartItemDTO request) {

        String email = extractUserEmail(authHeader);
        if (email == null) {
            return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để cập nhật giỏ hàng");
        }
        CartItemDTO updatedItem = cartItemService.updateCartItem(email, id, request.getQuantity());
        return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật giỏ hàng thành công", updatedItem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<?> removeFromCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long id) {

        try {
            String email = extractUserEmail(authHeader);
            if (email == null) {
                return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(),
                        "Vui lòng đăng nhập để xóa sản phẩm khỏi giỏ hàng");
            }

            cartItemService.removeFromCart(email, id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Xóa sản phẩm khỏi giỏ hàng thành công");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi xóa sản phẩm khỏi giỏ hàng",
                    e.getMessage());
        }
    }
}
