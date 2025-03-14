package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.CartItemRepository;
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

    private final CartItemRepository cartItemRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    public List<CartItemDTO> getUserCart(String email) {
        UserModel user = getUserByEmail(email);
        List<CartItemModel> cartItems = cartItemRepository.findByCustomerModel(user);

        return cartItems.stream()
                .map(cartItemMapper::toCartItemDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemDTO addToCart(String email, CartItemDTO request) {
        UserModel user = getUserByEmail(email);
        ProductModel product = getProductById(request.getProductId());

        // Check if item already exists in cart
        CartItemModel cartItem = cartItemRepository
                .findByCustomerModelAndProductModel(user, product)
                .orElse(null);

        if (cartItem == null) {
            cartItem = cartItemMapper.toCartItemModel(request);
            cartItem.setCustomerModel(user);
            cartItem.setProductModel(product);
            cartItem.setUnitPrice(product.getUnitPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        cartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemDTO(cartItem);
    }

    @Transactional
    public CartItemDTO updateCartItem(String email, Long cartItemId, Integer quantity) {
        UserModel user = getUserByEmail(email);
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
        UserModel user = getUserByEmail(email);
        CartItemModel cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Verify ownership
        if (!cartItem.getCustomerModel().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(String email) {
        UserModel user = getUserByEmail(email);
        cartItemRepository.deleteByCustomerModel(user);
    }

    private UserModel getUserByEmail(String email) {
        UserModel user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }

    private ProductModel getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}