package com.customer.data.request;

import java.io.Serializable;

public class CustomerRequest implements Serializable {

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String postalCode;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
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
