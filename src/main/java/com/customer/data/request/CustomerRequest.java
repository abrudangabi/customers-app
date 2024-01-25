package com.customer.data.request;

import java.io.Serializable;

public class CustomerRequest implements Serializable {

    Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    private AddressRequest currentLivingAddress;

    public CustomerRequest() {
    }

    public CustomerRequest(Long id, String firstName, String lastName, String email, Integer age, AddressRequest currentLivingAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.currentLivingAddress = currentLivingAddress;
    }

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
