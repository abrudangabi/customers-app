package com.customer.data.service;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.request.CustomerUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepositoryJpa customerRepository;

    public CustomerService(CustomerRepositoryJpa customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Get all customers from database
     * @return list of all customers
     */
    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    /**
     * Add a new customer in database
     * @param customerRequest - the customer to be saved in database
     * @return the saved customer
     * @throws CustomerValidationException the error message if the age of a customer is below 18 or the email already exists
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer addCustomer(CustomerRequest customerRequest) throws CustomerValidationException {
        LocalDate birthDate = LocalDate.parse(customerRequest.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        verifyCustomerAge(birthDate);

        Customer customer = Customer.builder().firstName(customerRequest.getFirstName()).lastName(customerRequest.getLastName())
                .age(birthDate).build();
        String email = customerRequest.getEmail();
        emailExists(email);

        AddressRequest addressRequest = customerRequest.getCurrentLivingAddress();

        if (email != null) {
            if (!email.isEmpty()) {
                customer.setEmail(email);
            }
        }
        if (addressRequest != null) {
            Address address = Address.builder().country(customerRequest.getCurrentLivingAddress().getCountry()).city(customerRequest.getCurrentLivingAddress().getCity())
                    .street(customerRequest.getCurrentLivingAddress().getStreet()).houseNumber(customerRequest.getCurrentLivingAddress().getHouseNumber())
                    .postalCode(customerRequest.getCurrentLivingAddress().getPostalCode()).build();
            customer.setCurrentLivingAddress(address);
        }

        return customerRepository.save(customer);
    }

    /**
     * Get customer by its id
     * @param id - id for the searched customer
     * @return customer by its id
     * @throws CustomerValidationException the error message that the searched id doesn't exist
     */
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) throws CustomerValidationException {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        } else {
            throw new CustomerValidationException("The customer with id " + id + " doesn't exist");
        }
    }

    /**
     * Update a customer
     * @param customerRequest - the customer information to be updated
     * @param id - the id of the updated customer
     * @return the updated customer
     * @throws CustomerValidationException the error message if email already exists or customer cannot be found by id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer updateCustomer(CustomerUpdateRequest customerRequest, Long id) throws CustomerValidationException {
        Customer foundCustomer = getCustomerById(id);

        String email = customerRequest.getEmail();
        emailExists(email);
        AddressRequest addressRequest = customerRequest.getCurrentLivingAddress();

        if (email == null) {
            foundCustomer.setEmail(null);
        } else if (email.isEmpty()) {
            foundCustomer.setEmail(null);
        } else {
            foundCustomer.setEmail(email);
        }
        if (addressRequest != null) {
            Address address = Address.builder().country(customerRequest.getCurrentLivingAddress().getCountry()).city(customerRequest.getCurrentLivingAddress().getCity())
                    .street(customerRequest.getCurrentLivingAddress().getStreet()).houseNumber(customerRequest.getCurrentLivingAddress().getHouseNumber())
                    .postalCode(customerRequest.getCurrentLivingAddress().getPostalCode()).build();
            foundCustomer.setCurrentLivingAddress(address);
        } else {
            foundCustomer.setCurrentLivingAddress(null);
        }

        return customerRepository.save(foundCustomer);
    }

    /**
     * Get customers by searching after their first name or last name
     * @param name - the first or last name for searched customers
     * @return list of searched customers
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomerByName(String name) {
        return customerRepository.findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(name, name);
    }

    /**
     * Verify if the email exists
     * If email already exist, an error message will be sent
     * @param email - the searched email to be verified
     * @throws CustomerValidationException the error message that the email already exists
     */
    @Transactional(readOnly = true)
    private void emailExists(String email) throws CustomerValidationException {
        List<Customer> foundCustomer = customerRepository.findByEmail(email);
        if (!foundCustomer.isEmpty() && email != null) {
            throw new CustomerValidationException("This email already exists!");
        }
    }

    /**
     * Verify if the customer age is below 18
     * @param birthDate - the birthdate of the customer
     * @throws CustomerValidationException the error message that the birthdate is below the age of 18 years
     */
    private void verifyCustomerAge(LocalDate birthDate) throws CustomerValidationException {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);

        int age = period.getYears();
        if (age < 18) {
            throw new CustomerValidationException("Customer age is smaller than 18!");
        }
    }
}
