package com.grocerystore.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email; // Email the OTP was sent to
    private String otp; // OTP code (usually 6 digits)
    private LocalDateTime createdAt; // Timestamp when OTP was generated
    private LocalDateTime expiresAt; // Timestamp when OTP will expire
    private boolean used; // Whether this OTP has already been used
}
