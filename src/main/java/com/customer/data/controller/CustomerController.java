package com.customer.data.controller;

import com.customer.data.controller.validation.DateValidator;
import com.customer.data.controller.validation.DateValidatorUsingDateFormat;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CreateCustomerRequest;
import com.customer.data.request.UpdateCustomerRequest;
import com.customer.data.response.CustomerResponse;
import com.customer.data.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    protected static final Logger logger = LogManager.getLogger();

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> retrieveAll() {
        return new ResponseEntity<>(this.customerService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@RequestBody @Valid CreateCustomerRequest customer) throws CustomerValidationException {
        customerValidation(customer);
        return new ResponseEntity<>(this.customerService.addCustomer(customer), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> retrieveById(@PathVariable @Min(1) Long id) throws CustomerValidationException {
        return new ResponseEntity<>(this.customerService.getCustomerById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@RequestBody @Valid UpdateCustomerRequest customerRequest, @PathVariable @Valid Long id)
            throws CustomerValidationException {
        isEmailAndAddressEmpty(customerRequest.getEmail(), customerRequest.getCurrentLivingAddress());
        return new ResponseEntity<>(this.customerService.updateCustomer(customerRequest, id), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CustomerResponse>> searchByFirstOrLastName(@PathVariable @NotBlank String name) {
        return new ResponseEntity<>(this.customerService.getCustomerByName(name), HttpStatus.OK);
    }

    private void customerValidation(CreateCustomerRequest customer) throws CustomerValidationException {
        String birthDate = customer.getBirthDate();
        String dateFormat = "yyyy-MM-dd";
        DateValidator validator = new DateValidatorUsingDateFormat(dateFormat);
        isEmailAndAddressEmpty(customer.getEmail(), customer.getCurrentLivingAddress());
        if (!validator.isValid(birthDate)) {
            logger.error("Birth date must be in " + dateFormat + " format");
            throw new CustomerValidationException("Birth date must be in " + dateFormat + " format");
        }
    }

    private void isEmailAndAddressEmpty(String email, AddressRequest addressRequest) throws CustomerValidationException {
        if (addressRequest == null) {
            if (email == null) {
                logger.error("The customer email and living address are empty!");
                throw new CustomerValidationException("Customer email and living address are empty!");
            }
            if (email.isBlank()) {
                logger.error("The customer email and living address are empty!");
                throw new CustomerValidationException("Customer email and living address are empty!");
            }
        }
    }
}
