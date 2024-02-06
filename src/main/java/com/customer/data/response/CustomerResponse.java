package com.customer.data.response;

import lombok.*;

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

    private AddressResponse currentLivingAddress;
}
