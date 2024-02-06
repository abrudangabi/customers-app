package com.customer.data.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String postalCode;

}
