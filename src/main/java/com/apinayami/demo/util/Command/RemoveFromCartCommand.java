package com.apinayami.demo.util.Command;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoveFromCartCommand implements CartCommand<CartItemDTO> {
    private final String email;
    private final Long cartItemId;

    private final ICartItemRepository cartItemRepository;
    private final IUserRepository userRepository;

    @Override
    public CartItemDTO execute() {
      UserModel user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }        
        CartItemModel cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCustomerModel().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this cart item");
        }

        cartItemRepository.delete(cartItem);
        return null;
    }
}
