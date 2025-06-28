package com.grocerystore.backend.dto;

import jakarta.validation.constraints.Size;

public class ProfileRequest {

    private String name;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    public ProfileRequest() {
    }

    public ProfileRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
