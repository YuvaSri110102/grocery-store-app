package com.grocerystore.backend.controller;

import com.grocerystore.backend.dto.CartRequest;
import com.grocerystore.backend.model.CartItem;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartItem addToCart(@AuthenticationPrincipal User user, @RequestBody CartRequest request) {
        return cartService.addToCart(user.getId(), request.getProductId(), request.getQuantity());
    }

    @GetMapping("/all")
    public List<CartItem> getCartItems(@AuthenticationPrincipal User user) {
        return cartService.getUserCart(user.getId());
    }

    @DeleteMapping("/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }

    @PutMapping("/update/{id}")
    public CartItem updateQuantity(
            @PathVariable Long id,
            @RequestBody CartRequest req) {
        return cartService.updateCartItem(id, req.getQuantity());
    }
}
