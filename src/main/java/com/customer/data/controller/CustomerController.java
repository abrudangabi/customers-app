package com.customer.data.controller;

import com.customer.data.entity.Customer;
import com.customer.data.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/retrieve-all")
    public List<Customer> retrieveAll() {
        return this.customerService.getAll();
    }

    @PostMapping("/add")
    public Customer retrieveAll() {
        return this.customerService.getAll();
    }
}
