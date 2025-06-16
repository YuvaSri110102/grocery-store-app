package com.grocerystore.backend.controller;

import com.grocerystore.backend.dto.AddressRequest;
import com.grocerystore.backend.dto.ProfileRequest;
import com.grocerystore.backend.model.Address;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.repository.UserRepository;
import com.grocerystore.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;


    // GET /api/user/profile
    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "message", "User profile fetched successfully"
        );
    }

    // PUT /api/user/profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileRequest request, BindingResult bindingResult, Authentication auth) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("message", errorMsg));
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "message", "Profile updated successfully"
        ));
    }


    // Add address - POST /api/user/address
    @PostMapping("/address")
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressRequest request, Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Address address = addressService.addAddress(request, user);
        return ResponseEntity.ok(Map.of("message", "Address added successfully", "address", address));
    }

    // Update address - POST /api/user/address/{id}
    @PutMapping("/address/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request, Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Address address = addressService.updateAddress(id, request, user);
        return ResponseEntity.ok(Map.of("message", "Address updated successfully", "address", address));
    }

    // Delete address - POST /api/user/address/{id}
    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        addressService.deleteAddress(id, user);
        return ResponseEntity.ok(Map.of("message", "Address deleted successfully"));
    }

    // Get all addresses - POST /api/user/addresses
    @GetMapping("/addresses")
    public ResponseEntity<?> getAllAddresses(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Address> addresses = addressService.getUserAddresses(user);
        return ResponseEntity.ok(Map.of("addresses", addresses));
    }

}
