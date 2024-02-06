package com.customer.data.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class AddressRequest implements Serializable {

    @NotBlank(message = "Country cannot be empty or null")
    private String country;

    @NotBlank(message = "City cannot be empty or null")
    private String city;

    @NotBlank(message = "Street cannot be empty or null")
    private String street;

    @NotBlank(message = "House number cannot be empty or null")
    private String houseNumber;

    @NotBlank(message = "Postal code cannot be empty or null")
    private String postalCode;

    public AddressRequest() {
    }

    public AddressRequest(String country, String city, String street, String houseNumber, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
