package com.grocerystore.backend.repository;

import com.grocerystore.backend.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByEmailAndOtpOrderByCreatedAtDesc(String email, String otp);
}
