package com.apinayami.demo.util.Command;


import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddToCartCommand implements CartCommand<CartItemDTO> {

    private final String email;
    private final CartItemDTO request;

    private final ICartItemRepository cartItemRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDTO execute() {
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null) throw new ResourceNotFoundException("User not found");

        ProductModel product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItemModel cartItem = cartItemRepository
                .findByCustomerModelAndProductModel(user, product)
                .orElse(null);

        if (cartItem == null) {
            cartItem = cartItemMapper.toCartItemModel(request);
            cartItem.setCustomerModel(user);
            cartItem.setProductModel(product);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        cartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemDTO(cartItem);
    }
}
