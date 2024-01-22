package com.customer.data.controller;

import com.customer.data.entity.Customer;
import com.customer.data.service.CustomerService;
import org.springframework.web.bind.annotation.*;

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
    public Customer add(@RequestBody Customer customer) {
        return this.customerService.addCustomer(customer);
    }

//    @GetMapping("retrieve-by/{id}")
//    public Customer retrieveById(@PathVariable Long id) {
//        return new Customer();
//    }
//
//    @PutMapping("")
//    public void update() {
//        //
//    }
//
//    @GetMapping("")
//    public Customer searchByFirstOrLastName(@RequestParam String name) {
//        return new Customer();
//    }
}
