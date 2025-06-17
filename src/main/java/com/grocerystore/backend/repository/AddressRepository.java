package com.grocerystore.backend.repository;

import com.grocerystore.backend.model.Address;
import com.grocerystore.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}


