package com.grocerystore.backend.dto;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String landmark;
    private String type;
}
