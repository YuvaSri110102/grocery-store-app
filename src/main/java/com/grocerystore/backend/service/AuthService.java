package com.grocerystore.backend.service;

import com.grocerystore.backend.dto.ForgotPasswordRequest;
import com.grocerystore.backend.dto.LoginRequest;
import com.grocerystore.backend.dto.RegisterRequest;
import com.grocerystore.backend.dto.ResetPasswordRequest;
import com.grocerystore.backend.model.OtpCode;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.repository.OtpRepository;
import com.grocerystore.backend.repository.UserRepository;
import com.grocerystore.backend.security.JwtUtil;
import com.grocerystore.backend.util.OtpUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpUtil otpUtil;
    private final EmailService emailService;

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> res = new HashMap<>();

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            res.put("success", false);
            res.put("message", "Email Already Exists. Please Login to Access");
            return res;
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        res.put("success", true);
        res.put("message", "User Registered Successfully");
        return res;
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) return "User not found";

        String otp = otpUtil.generateOtp(); // Generate 6-digit OTP

        OtpCode code = OtpCode.builder()
                .email(request.getEmail())
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10)) // valid for 10 minutes
                .used(false)
                .build();

        otpRepository.save(code);

        // Simulate email sending — in real case, you'd use a mail service
//         System.out.println("Sending OTP to email: " + otp);
//        emailService.sendOtpEmail(request.getEmail(), otp);
        return "OTP sent to your email!";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        Optional<OtpCode> otpCodeOpt = otpRepository.findTopByEmailAndOtpOrderByCreatedAtDesc(request.getEmail(), request.getOtp());

        if (otpCodeOpt.isEmpty()) return "Invalid OTP";

        OtpCode code = otpCodeOpt.get();

        if (code.isUsed()) return "OTP already used";
        if (code.getExpiresAt().isBefore(LocalDateTime.now())) return "OTP expired";

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) return "User not found";

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        code.setUsed(true);
        otpRepository.save(code);

        return "Password reset successful!";
    }

}
