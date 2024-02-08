package com.customer.data.response;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    private Instant createdOn;

    private Instant lastUpdatedOn;

    private AddressResponse currentLivingAddress;
}
