package com.customer.data.response;

import com.customer.data.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class AddressResponse {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String houseNumber;

    @Column
    private String postalCode;

    @OneToOne(mappedBy = "currentLivingAddress")
    @JsonIgnore
    private Customer customer;
}
