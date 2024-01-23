package com.customer.data.controller;

import com.customer.data.entity.Customer;
import com.customer.data.request.CustomerRequest;
import com.customer.data.service.CustomerService;
import jakarta.validation.Valid;
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
    public List<Customer> retrieveAll() {
        return this.customerService.getAll();
    }

    @PostMapping
    public Customer add(@RequestBody CustomerRequest customer) {
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
