package com.customer.data.controller;

import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.request.CustomerRequest;
import com.customer.data.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity retrieveAll() {
        return new ResponseEntity(this.customerService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerRequest customer) throws CustomerValidationException {
        return new ResponseEntity(this.customerService.addCustomer(customer), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity retrieveById(@PathVariable Long id) {
        return new ResponseEntity(this.customerService.getCustomerById(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody @Valid CustomerRequest customerRequest) throws CustomerValidationException {
        return new ResponseEntity(this.customerService.updateCustomer(customerRequest), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity searchByFirstOrLastName(@PathVariable String name) {
        return new ResponseEntity(this.customerService.getCustomerByName(name), HttpStatus.OK);
    }
}
