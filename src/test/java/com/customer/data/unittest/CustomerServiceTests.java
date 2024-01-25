package com.customer.data.unittest;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.service.CustomerService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepositoryJpa repository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void getAllCustomersTest(){
        // given - precondition or setup

        Customer customer = new Customer("Gabi", "Abrudan", "gabi@yahoo.com", 27);
        List<Customer> customerList = Arrays.asList(customer);

        when(repository.findAll()).thenReturn(customerList);

        // when -  action or the behaviour that we are going test
        List<Customer> result = customerService.getAll();

        for (Customer c : result) {
            System.out.println(c);
        }

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.get(0).getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.get(0).getAge()).isEqualTo(customer.getAge());
    }

    @Test
    public void addCustomerWithoutAddressTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressRequest);

        Customer customer = new Customer("Gabi", "Abrudan", "gabi@yahoo.com", 27);

        when(repository.save(any(Customer.class))).thenReturn(customer);

        // when -  action or the behaviour that we are going test
        Customer result = customerService.addCustomer(customerRequest);
        System.out.println(result);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void addCustomerWithoutEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);

        // when -  action or the behaviour that we are going test
        Customer result = customerService.addCustomer(customerRequest);
        System.out.println(result);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo("");
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNotNull();
        assertThat(result.getCurrentLivingAddress().getCountry()).isEqualTo(address.getCountry());
        assertThat(result.getCurrentLivingAddress().getCity()).isEqualTo(address.getCity());
        assertThat(result.getCurrentLivingAddress().getStreet()).isEqualTo(address.getStreet());
        assertThat(result.getCurrentLivingAddress().getHouseNumber()).isEqualTo(address.getHouseNumber());
        assertThat(result.getCurrentLivingAddress().getPostalCode()).isEqualTo(address.getPostalCode());
    }

    @Test
    public void addCustomerEmptyAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = null;
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("Both email and address are empty!", exception.getMessage());
    }

    @Test
    public void addCustomerEmptyCountryAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest("", "Iasi", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("Customer country address is empty!", exception.getMessage());
    }

    @Test
    public void addCustomerEmptyFirstNameTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = null;
        CustomerRequest customerRequest = new CustomerRequest(1L, "", "Abrudan", "gabi@yahoo.com", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("Customer first name is empty!", exception.getMessage());
    }

    @Test
    public void getCustomerByIdTest(){
        // given - precondition or setup

        Customer customer = new Customer("Gabi", "Abrudan", "gabi@yahoo.com", 27);
        List<Customer> customerList = Arrays.asList(customer);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.getCustomerById(1L);

        // then - verify the output
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    public void getCustomerByFirstNameTest(){
        // given - precondition or setup

        Customer customer = new Customer("Gabi", "Abrudan", "gabi@yahoo.com", 27);
        List<Customer> customerList = Arrays.asList(customer);
        String firstName = "Gabi";

        when(repository.findByFirstName(any(String.class))).thenReturn(customerList);

        // when -  action or the behaviour that we are going test
        List<Customer> result = customerService.getCustomerByName(firstName);

        for (Customer c : result) {
            System.out.println(c);
        }

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.get(0).getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.get(0).getAge()).isEqualTo(customer.getAge());
    }

    @Test
    public void updateCustomerWithoutAddressTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "gabi@yahoo.com", 27, addressRequest);

        Customer customer = new Customer("Gabi", "Abrudan", "gabi@yahoo.com", 27);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.updateCustomer(customerRequest);
        System.out.println(result);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void updateCustomerWithoutEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.updateCustomer(customerRequest);
        System.out.println(result);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo("");
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNotNull();
        assertThat(result.getCurrentLivingAddress().getCountry()).isEqualTo(address.getCountry());
        assertThat(result.getCurrentLivingAddress().getCity()).isEqualTo(address.getCity());
        assertThat(result.getCurrentLivingAddress().getStreet()).isEqualTo(address.getStreet());
        assertThat(result.getCurrentLivingAddress().getHouseNumber()).isEqualTo(address.getHouseNumber());
        assertThat(result.getCurrentLivingAddress().getPostalCode()).isEqualTo(address.getPostalCode());
    }

    @Test
    public void updateCustomerEmptyAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = null;
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.updateCustomer(customerRequest);
        });

        assertEquals("Both email and address are empty!", exception.getMessage());
    }

    @Test
    public void updateCustomerEmptyCityAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest("Rom", "", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "Abrudan", "", 27, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer("Gabi", "Abrudan", "", 27);
        customer.setCurrentLivingAddress(address);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.updateCustomer(customerRequest);
        });

        assertEquals("Customer city address is empty!", exception.getMessage());
    }

    @Test
    public void updateCustomerEmptyLastNameTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = null;
        CustomerRequest customerRequest = new CustomerRequest(1L, "Gabi", "", "gabi@yahoo.com", 27, addressRequest);

        Customer customer = new Customer("Gabi", "Abrudan", "", 27);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.updateCustomer(customerRequest);
        System.out.println(result);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

}
