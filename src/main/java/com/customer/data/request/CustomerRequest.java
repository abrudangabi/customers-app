package com.customer.data.request;

import java.io.Serializable;

public class CustomerRequest implements Serializable {

    Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    private AddressRequest currentLivingAddress;

    public Long getId() {
        return id;
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

    public Integer getAge() {
        return age;
    }

    public AddressRequest getCurrentLivingAddress() {
        return currentLivingAddress;
    }
}
