package com.customer.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Customer {

    @Id
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    @OneToOne
    private Address currentLivingAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getCurrentLivingAddress() {
        return currentLivingAddress;
    }

    public void setCurrentLivingAddress(Address currentLivingAddress) {
        this.currentLivingAddress = currentLivingAddress;
    }
}
