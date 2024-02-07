package com.customer.data.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest implements Serializable {

    @NotBlank(message = "First name cannot be empty or null")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty or null")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String birthDate;

    @Valid
    private AddressRequest currentLivingAddress;
}
