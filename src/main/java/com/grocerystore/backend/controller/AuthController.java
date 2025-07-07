package com.grocerystore.backend.controller;

import com.grocerystore.backend.dto.RegisterRequest;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

}
