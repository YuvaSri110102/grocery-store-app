package com.grocerystore.backend.repository;

import com.grocerystore.backend.model.CartItem;
import com.grocerystore.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
}
