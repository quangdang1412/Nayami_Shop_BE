package com.apinayami.demo.util.Command;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.CartModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICartRepository;
import com.apinayami.demo.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateCartItemCommand implements CartCommand<CartItemDTO> {
    private final String email;
    private final Long cartItemId;
    private final Integer quantity;

    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDTO execute() {
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        CartModel cart = cartRepository.findByCustomerModel(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        CartItemModel cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cart.getCustomerModel().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this cart item");
        }

        if (quantity <= 0) {
            cart.getCartItems().remove(cartItem);
            cartRepository.save(cart);
            return null;
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartItemMapper.toCartItemDTO(cartItem);
    }
}
