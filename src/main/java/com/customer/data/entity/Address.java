package com.customer.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.util.CustomObjectInputStream;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Address {

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
