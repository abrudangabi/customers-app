package com.customer.data.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public class UpdateCustomerRequest {

    @Email(message = "Email should be valid")
    private String email;

    @Valid
    private AddressRequest currentLivingAddress;

    public UpdateCustomerRequest(String email, AddressRequest currentLivingAddress) {
        this.email = email;
        this.currentLivingAddress = currentLivingAddress;
    }

    public String getEmail() {
        return email;
    }

    public AddressRequest getCurrentLivingAddress() {
        return currentLivingAddress;
    }
}
