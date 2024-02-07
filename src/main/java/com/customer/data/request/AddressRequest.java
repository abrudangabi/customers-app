package com.customer.data.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest implements Serializable {

    @NotBlank(message = "Country cannot be empty or null")
    private String country;

    @NotBlank(message = "City cannot be empty or null")
    private String city;

    @NotBlank(message = "Street cannot be empty or null")
    private String street;

    @NotBlank(message = "House number cannot be empty or null")
    private String houseNumber;

    @NotBlank(message = "Postal code cannot be empty or null")
    private String postalCode;
}
