package com.customer.data.response;

import com.customer.data.entity.Address;
import jakarta.persistence.*;

public class CustomerResponse {

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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", nullable = true)
    private Address currentLivingAddress;
}
