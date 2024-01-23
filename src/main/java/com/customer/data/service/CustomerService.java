package com.customer.data.service;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.CustomerRequest;
import com.customer.data.request.UpdateCustomerRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepositoryJpa customerRepository;

    public CustomerService(CustomerRepositoryJpa customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(CustomerRequest customerRequest) throws CustomerValidationException {
        Customer customer = new Customer(customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getEmail(), customerRequest.getAge());
        if (customerValidation(customer)) {
            Address address = new Address(customerRequest.getCountry(), customerRequest.getCity(), customerRequest.getStreet(),
                    customerRequest.getHouseNumber(), customerRequest.getPostalCode());
            // If email exists, then the customer is saved without address
            if (!"".equals(customer.getEmail()) && customer.getEmail() != null) {
                return customerRepository.save(customer);
            } else if (addressValidation(address)) {
                customer.setCurrentLivingAddress(address);
                return customerRepository.save(customer);
            }
        }
        return null;
    }

    public Customer getCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.orElse(null);
    }

    public Customer updateCustomer(CustomerRequest customerRequest) throws CustomerValidationException {
        Customer foundCustomer = getCustomerById(customerRequest.getId());

        String email = customerRequest.getEmail();
        Address address = new Address(customerRequest.getCountry(), customerRequest.getCity(), customerRequest.getStreet(),
                customerRequest.getHouseNumber(), customerRequest.getPostalCode());

        if (!"".equals(email)) {
            foundCustomer.setEmail(email);
        } else {
            if (!addressValidation(address)) {
                throw new CustomerValidationException("Both email and address are empty!");
            } else {
                foundCustomer.setCurrentLivingAddress(address);
            }
        }

        return customerRepository.save(foundCustomer);
    }

    public List<Customer> getCustomerByName(String name) {
        List<Customer> customerList = customerRepository.findByFirstName(name);
        customerList.addAll(customerRepository.findByLastName(name));
        return customerList;
    }

    private boolean customerValidation(Customer customer) throws CustomerValidationException {
        if ("".equals(customer.getFirstName()) || customer.getFirstName() == null || "".equals(customer.getLastName()) || customer.getLastName() == null) {
            throw new CustomerValidationException("Customer first name or last name is empty!");
        }
        if (customer.getAge() < 18) {
            throw new CustomerValidationException("Customer age is smaller than 18!");
        }
        return true;
    }

    private boolean addressValidation(Address address) throws CustomerValidationException {
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

    private boolean addressFieldsValidation(Address address) throws CustomerValidationException {
        if ("".equals(address.getCountry()) || address.getCountry() == null) {
            throw new CustomerValidationException("Customer country address is empty!");
        }
        if ("".equals(address.getCity()) || address.getCity() == null) {
            throw new CustomerValidationException("Customer city address is empty!");
        }
        if ("".equals(address.getStreet()) || address.getStreet() == null) {
            throw new CustomerValidationException("Customer street address is empty!");
        }
        if ("".equals(address.getHouseNumber()) || address.getHouseNumber() == null) {
            throw new CustomerValidationException("Customer house number address is empty!");
        }
        if ("".equals(address.getPostalCode()) || address.getPostalCode() == null) {
            throw new CustomerValidationException("Customer postal code address is empty!");
        }
        return true;
    }
}
