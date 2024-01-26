package com.customer.data.service;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
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
        Customer customer = new Customer(customerRequest.getId(), customerRequest.getFirstName(), customerRequest.getLastName(),
                "", customerRequest.getAge());
        String email = customerRequest.getEmail();
        Long id = customerRequest.getId();

        Customer foundCustomer = getCustomerById(id);

        if (foundCustomer != null) {
            throw new CustomerValidationException("The customer with id " + id + " already exists");
        }

        if (customerValidation(customer)) {
            AddressRequest addressRequest = customerRequest.getCurrentLivingAddress();

            // If email exists, then the customer is saved without address
            if (!"".equals(email) && email != null) {
                customer.setEmail(email);
            } else {
                if (addressRequest == null) {
                    throw new CustomerValidationException("Both email and address are empty!");
                } else {
                    Address address = new Address(customerRequest.getCurrentLivingAddress().getCountry(), customerRequest.getCurrentLivingAddress().getCity(),
                            customerRequest.getCurrentLivingAddress().getStreet(), customerRequest.getCurrentLivingAddress().getHouseNumber(),
                            customerRequest.getCurrentLivingAddress().getPostalCode());

                    addressFieldsValidation(address);
                    customer.setCurrentLivingAddress(address);
                }
            }
        }
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.orElse(null);
    }

    public Customer updateCustomer(CustomerRequest customerRequest) throws CustomerValidationException {
        Customer foundCustomer = getCustomerById(customerRequest.getId());

        String email = customerRequest.getEmail();
        AddressRequest addressRequest = customerRequest.getCurrentLivingAddress();

        if (foundCustomer == null) {
            throw new CustomerValidationException("The customer with id " + customerRequest.getId() + " doesn't exists");
        }

        if (!"".equals(email)) {
            foundCustomer.setEmail(email);
        } else {
            if (addressRequest == null) {
                throw new CustomerValidationException("Both email and address are empty!");
            } else {
                Address address = new Address(customerRequest.getCurrentLivingAddress().getCountry(), customerRequest.getCurrentLivingAddress().getCity(),
                        customerRequest.getCurrentLivingAddress().getStreet(), customerRequest.getCurrentLivingAddress().getHouseNumber(),
                        customerRequest.getCurrentLivingAddress().getPostalCode());

                addressFieldsValidation(address);
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
        if (customer.getId() == null || customer.getId() < 1) {
            throw new CustomerValidationException("Customer id is incorrect!");
        }
        if ("".equals(customer.getFirstName()) || customer.getFirstName() == null) {
            throw new CustomerValidationException("Customer first name is empty!");
        }
        if ("".equals(customer.getLastName()) || customer.getLastName() == null) {
            throw new CustomerValidationException("Customer last name is empty!");
        }
        if (customer.getAge() < 18) {
            throw new CustomerValidationException("Customer age is smaller than 18!");
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
