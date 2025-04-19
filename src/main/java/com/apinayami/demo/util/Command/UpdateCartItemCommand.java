package com.apinayami.demo.util.Command;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateCartItemCommand implements CartCommand<CartItemDTO> {
    private final String email;
    private final Long cartItemId;
    private final Integer quantity;

    private final ICartItemRepository cartItemRepository;
    private final IUserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    @Override
    public CartItemDTO execute(){
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }        
        CartItemModel cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        if (!cartItem.getCustomerModel().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this cart item");
        }
        
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }
        
        cartItem.setQuantity(quantity);
        cartItem = cartItemRepository.save(cartItem);
        
        return cartItemMapper.toCartItemDTO(cartItem);
    }
}
