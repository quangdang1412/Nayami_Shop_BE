package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.ICartItemService;

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
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }        
        ProductModel product = getProductById(request.getProductId());

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

    @Transactional
    public CartItemDTO updateCartItem(String email, Long cartItemId, Integer quantity) {
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
    @Transactional
    public void removeFromCart(String email, Long cartItemId) {
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
    }
    private ProductModel getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}