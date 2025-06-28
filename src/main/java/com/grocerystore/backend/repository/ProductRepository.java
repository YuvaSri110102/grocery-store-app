package com.grocerystore.backend.repository;

import com.grocerystore.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDeletedFalse(); // For admin listing only active products
}
