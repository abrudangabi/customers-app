package com.customer.data.service;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.request.CustomerUpdateRequest;
import com.customer.data.response.AddressResponse;
import com.customer.data.response.CustomerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<CustomerResponse> getAll() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList.stream().map(this::createCustomerResponse).collect(Collectors.toList());
    }

    /**
     * Add a new customer in database
     * @param customerRequest - the customer to be saved in database
     * @return the saved customer
     * @throws CustomerValidationException the error message if the age of a customer is below 18 or the email already exists
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerResponse addCustomer(CustomerRequest customerRequest) throws CustomerValidationException {
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

        Customer savedCustomer = customerRepository.save(customer);
        return createCustomerResponse(savedCustomer);
    }

    /**
     * Find customer by its id in database
     * @param id - id for the searched customer
     * @return customer by its id
     * @throws CustomerValidationException the error message that the searched id doesn't exist
     */
    @Transactional(readOnly = true)
    private Customer findCustomerById(Long id) throws CustomerValidationException {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        } else {
            throw new CustomerValidationException("The customer with id " + id + " doesn't exist");
        }
    }

    /**
     * Get customer by its id
     * @param id - id for the searched customer
     * @return customer response by its id
     * @throws CustomerValidationException the error message that the searched id doesn't exist
     */
    public CustomerResponse getCustomerById(Long id) throws CustomerValidationException {
        Customer savedCustomer = findCustomerById(id);
        return createCustomerResponse(savedCustomer);
    }

    /**
     * Update a customer
     * @param customerRequest - the customer information to be updated
     * @param id - the id of the updated customer
     * @return the updated customer
     * @throws CustomerValidationException the error message if email already exists or customer cannot be found by id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerResponse updateCustomer(CustomerUpdateRequest customerRequest, Long id) throws CustomerValidationException {
        Customer foundCustomer = findCustomerById(id);

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

        Customer savedCustomer = customerRepository.save(foundCustomer);
        return createCustomerResponse(savedCustomer);
    }

    /**
     * Transform Customer object to CustomerResponse object
     * @param customer - the Customer object
     * @return the CustomerResponse object
     */
    private CustomerResponse createCustomerResponse(Customer customer) {
        CustomerResponse response = CustomerResponse.builder().id(customer.getId()).firstName(customer.getFirstName()).lastName(customer.getLastName())
                .email(customer.getEmail()).age(calculateAge(customer.getAge())).build();
        if (customer.getCurrentLivingAddress() != null) {
            AddressResponse addressResponse = AddressResponse.builder().country(customer.getCurrentLivingAddress().getCountry()).city(customer.getCurrentLivingAddress().getCity())
                    .street(customer.getCurrentLivingAddress().getStreet()).houseNumber(customer.getCurrentLivingAddress().getHouseNumber())
                    .postalCode(customer.getCurrentLivingAddress().getPostalCode()).build();
            response.setCurrentLivingAddress(addressResponse);
        }
        return response;
    }

    /**
     * Get customers by searching after their first name or last name
     * @param name - the first or last name for searched customers
     * @return list of searched customers
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomerByName(String name) {
        List<Customer> customerList = customerRepository.findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(name, name);
        return customerList.stream().map(this::createCustomerResponse).collect(Collectors.toList());
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
        int age = calculateAge(birthDate);
        if (age < 18) {
            throw new CustomerValidationException("Customer age is smaller than 18!");
        }
    }

    /**
     * Calculate the age in years
     * @param birthDate - the birthdate
     * @return the age of the customer
     */
    private static int calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);

        return period.getYears();
    }
}
