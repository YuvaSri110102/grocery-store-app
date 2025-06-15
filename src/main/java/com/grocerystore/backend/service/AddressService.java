package com.grocerystore.backend.service;

import com.grocerystore.backend.dto.AddressRequest;
import com.grocerystore.backend.model.Address;
import com.grocerystore.backend.model.User;
import com.grocerystore.backend.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address addAddress(AddressRequest request, User user) {
        Address address = Address.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .country(request.getCountry())
                .landmark(request.getLandmark())
                .type(request.getType())
                .user(user)
                .build();

        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, AddressRequest request, User user) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this address");
        }

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setLandmark(request.getLandmark());
        address.setType(request.getType());

        return addressRepository.save(address);
    }

    public void deleteAddress(Long id, User user) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this address");
        }

        addressRepository.delete(address);
    }

    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

}
