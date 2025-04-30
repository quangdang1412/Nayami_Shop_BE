package com.apinayami.demo.util.Command;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.CartModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.repository.ICartRepository;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddToCartCommand implements CartCommand<CartItemDTO> {

    private final String email;
    private final CartItemDTO request;

    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final ICartRepository cartRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDTO execute() {
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null)
            throw new ResourceNotFoundException("User not found");

        CartModel cart = cartRepository.findByCustomerModel(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        ProductModel product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItemModel cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductModel().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = cartItemMapper.toCartItemModel(request);
            cartItem.setProductModel(product);
            cartItem.setCartModel(cart);
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        cart = cartRepository.save(cart);

        CartItemModel savedCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductModel().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to save cart item"));

        return cartItemMapper.toCartItemDTO(savedCartItem);
    }
}
