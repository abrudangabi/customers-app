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

    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    // todo: lipsesc logurile
    // todo: in loc de age salvez cu data nasterii
    // todo: si mai adaug campurile createdBy, createDate ...
    // todo: monitoring
    // todo: adaugarea versiunii in url

    @Transactional(propagation = Propagation.REQUIRED)
    public Customer addCustomer(CustomerRequest customerRequest) throws CustomerValidationException {
        LocalDate birthDate = LocalDate.parse(customerRequest.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        verifyCustomerAge(birthDate);
        Customer customer = new Customer(customerRequest.getFirstName(), customerRequest.getLastName(),
                null, birthDate);
        String email = customerRequest.getEmail();
        emailExists(email);

        AddressRequest addressRequest = customerRequest.getCurrentLivingAddress();

        if (!"".equals(email) && email != null) {
            customer.setEmail(email);
        }
        if (addressRequest != null) {
            Address address = new Address(customerRequest.getCurrentLivingAddress().getCountry(), customerRequest.getCurrentLivingAddress().getCity(),
                    customerRequest.getCurrentLivingAddress().getStreet(), customerRequest.getCurrentLivingAddress().getHouseNumber(),
                    customerRequest.getCurrentLivingAddress().getPostalCode());
            customer.setCurrentLivingAddress(address);
        }

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) throws CustomerValidationException {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        } else {
            throw new CustomerValidationException("The customer with id " + id + " doesn't exists");
        }
    }

    /**/
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer updateCustomer(CustomerUpdateRequest customerRequest, Long id) throws CustomerValidationException {
        Customer foundCustomer = getCustomerById(id);

        String email = customerRequest.getEmail();
        emailExists(email);
        AddressRequest addressRequest = customerRequest.getCurrentLivingAddress();

        if (email == null) {
            foundCustomer.setEmail(null);
        } else if ("".equals(email)) {
            foundCustomer.setEmail(null);
        } else {
            foundCustomer.setEmail(email);
        }
        if (addressRequest != null) {
            Address address = new Address(customerRequest.getCurrentLivingAddress().getCountry(), customerRequest.getCurrentLivingAddress().getCity(),
                    customerRequest.getCurrentLivingAddress().getStreet(), customerRequest.getCurrentLivingAddress().getHouseNumber(),
                    customerRequest.getCurrentLivingAddress().getPostalCode());
            foundCustomer.setCurrentLivingAddress(address);
        } else {
            foundCustomer.setCurrentLivingAddress(null);
        }

        return customerRepository.save(foundCustomer);
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomerByName(String name) {
        List<Customer> customerList = customerRepository.findByFirstName(name);
        customerList.addAll(customerRepository.findByLastName(name));
        return customerList;
    }

    @Transactional(readOnly = true)
    private void emailExists(String email) throws CustomerValidationException {
        List<Customer> foundCustomer = customerRepository.findByEmail(email);
        if (!foundCustomer.isEmpty() && email != null) {
            throw new CustomerValidationException("This email already exists!");
        }
    }

    private void verifyCustomerAge(LocalDate birthDate) throws CustomerValidationException {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);

        System.out.println(period.getYears() + " years");
        int age = period.getYears();
        if (age < 18) {
            throw new CustomerValidationException("Customer age is smaller than 18!");
        }
    }
}
