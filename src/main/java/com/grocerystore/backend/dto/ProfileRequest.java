package com.grocerystore.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequest {
    private String name;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
