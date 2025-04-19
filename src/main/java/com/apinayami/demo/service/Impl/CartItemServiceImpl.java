package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.ICartItemService;
import com.apinayami.demo.util.Command.AddToCartCommand;
import com.apinayami.demo.util.Command.RemoveFromCartCommand;
import com.apinayami.demo.util.Command.UpdateCartItemCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    private final ICartItemRepository cartItemRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    public List<CartItemDTO> getUserCart(String email) {
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }        
        List<CartItemModel> cartItems = cartItemRepository.findByCustomerModel(user);

        return cartItems.stream()
                .map(cartItemMapper::toCartItemDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemDTO addToCart(String email, CartItemDTO request) {
        return new AddToCartCommand(email, request,cartItemRepository, userRepository, productRepository, cartItemMapper).execute();
    }

    @Transactional
    public CartItemDTO updateCartItem(String email, Long cartItemId, Integer quantity) {
        return new UpdateCartItemCommand(email, cartItemId, quantity, cartItemRepository, userRepository, cartItemMapper).execute();
        
    }
    @Transactional
    public void removeFromCart(String email, Long cartItemId) {
        new RemoveFromCartCommand(email, cartItemId, cartItemRepository, userRepository).execute();
    }
}