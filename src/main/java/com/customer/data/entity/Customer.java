package com.customer.data.entity;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String email;

    @Column
    private Integer age;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address currentLivingAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
