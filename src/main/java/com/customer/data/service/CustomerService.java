package com.customer.data.service;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.CustomerRequest;
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

    public Customer addCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer(customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getEmail(), customerRequest.getAge());
        if (customerValidation(customer)) {
            Address address = new Address(customerRequest.getCountry(), customerRequest.getCity(), customerRequest.getStreet(),
                    customerRequest.getHouseNumber(), customerRequest.getPostalCode());
            if (!"".equals(customer.getEmail()) && customer.getEmail() != null && addressValidation(address)) {
                customer.setCurrentLivingAddress(address);
                return customerRepository.save(customer);
            } else if (addressValidation(address)) {
                customer.setCurrentLivingAddress(address);
                return customerRepository.save(customer);
            }
        }
        return null;
    }

    private boolean customerValidation(Customer customer) {
        if ("".equals(customer.getFirstName()) || customer.getFirstName() == null || "".equals(customer.getLastName()) || customer.getLastName() == null) {
            return false;
        }
        if (customer.getAge() < 18) {
            return false;
        }
        return true;
    }

    private boolean addressValidation(Address address) {
        if ("".equals(address.getCountry()) || address.getCountry() == null) {
            return false;
        }
        if ("".equals(address.getCity()) || address.getCity() == null) {
            return false;
        }
        if ("".equals(address.getStreet()) || address.getStreet() == null) {
            return false;
        }
        if ("".equals(address.getHouseNumber()) || address.getHouseNumber() == null) {
            return false;
        }
        if ("".equals(address.getPostalCode()) || address.getPostalCode() == null) {
            return false;
        }
        return true;
    }
}
