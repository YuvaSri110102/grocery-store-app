package com.grocerystore.backend.repository;

import com.grocerystore.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique email.
     *
     * @param email the user's email address
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if user exists
     */
    boolean existsByEmail(String email);

    // Add more queries here when needed (e.g., findByPhone, existsByPhone, etc.)


}
