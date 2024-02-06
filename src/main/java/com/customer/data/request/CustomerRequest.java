package com.customer.data.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerRequest implements Serializable {

    @NotBlank(message = "First name cannot be empty or null")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty or null")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String birthDate;

    @Valid
    private AddressRequest currentLivingAddress;

    public CustomerRequest() {
    }

    public CustomerRequest(String firstName, String lastName, String email, String birthDate, AddressRequest currentLivingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.currentLivingAddress = currentLivingAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public AddressRequest getCurrentLivingAddress() {
        return currentLivingAddress;
    }
}
