package com.grocerystore.backend.controller;

import com.grocerystore.backend.dto.*;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.repository.UserRepository;
import com.grocerystore.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //  POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", errorMsg
            ));
        }

        Map<String, Object> serviceResponse = authService.register(request);

        boolean isSuccess = (boolean) serviceResponse.get("success");

        if (!isSuccess) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", serviceResponse.get("message")
            ));
        }
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", serviceResponse.get("message")
        ));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("message", errorMsg));
        }

        try {
            String jwt = authService.login(request);
            return ResponseEntity.ok().body(Map.of(
                    "token", jwt,
                    "message", "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }


    // POST /api/auth/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMsg);
        }
        String response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/auth/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMsg);
        }
        String response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

}

