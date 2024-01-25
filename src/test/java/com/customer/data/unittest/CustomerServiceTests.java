package com.customer.data.unittest;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.service.CustomerService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DataJpaTest
@RunWith(SpringRunner.class)
//@SpringBootTest(classes=MyApplication.class)
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepositoryJpa repository;

    @InjectMocks
    private CustomerService customerService;

    // write test cases here

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

}
