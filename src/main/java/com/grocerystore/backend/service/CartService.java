package com.grocerystore.backend.service;

import com.grocerystore.backend.exception.ResourceNotFoundException;
import com.grocerystore.backend.model.CartItem;
import com.grocerystore.backend.model.Product;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.repository.CartItemRepository;
import com.grocerystore.backend.repository.ProductRepository;
import com.grocerystore.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public CartItem addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        CartItem item = new CartItem(user, product, quantity, total);

        return cartItemRepository.save(item);
    }

    public List<CartItem> getUserCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return cartItemRepository.findByUser(user);
    }

    public void removeFromCart(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public CartItem updateCartItem(Long itemId, int quantity) {
        Optional<CartItem> optionalItem = cartItemRepository.findById(itemId);
      if (optionalItem.isEmpty()) throw new ResourceNotFoundException("Item not found");
        CartItem item = optionalItem.get();
        item.setQuantity(quantity);
        item.setTotalPrice(item.getProduct().getPrice().multiply (BigDecimal.valueOf(quantity)));
        return cartItemRepository.save(item);
    }
}
