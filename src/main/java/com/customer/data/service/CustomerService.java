package com.customer.data.service;

import com.customer.data.entity.Customer;
import com.customer.data.repository.CustomerRepositoryJpa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepositoryJpa customerRepository;

    public CustomerService(CustomerRepositoryJpa customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(Customer customer) {
        if (customerValidation(customer)) {
            return customerRepository.save(customer);
        }
        return null;
    }

    private boolean customerValidation(Customer customer) {
        if (customer.getFirstName().isEmpty() || customer.getLastName().isEmpty()) {
            return false;
        }
        if (customer.getAge() < 18) {
            return false;
        }
        if (customer.getEmail().isEmpty() || customer.getCurrentLivingAddress() == null) {
            return false;
        }
        return true;
    }
}
