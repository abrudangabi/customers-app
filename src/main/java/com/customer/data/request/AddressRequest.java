package com.customer.data.request;

import java.io.Serializable;

public class AddressRequest implements Serializable {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String postalCode;

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
