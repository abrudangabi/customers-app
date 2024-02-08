package com.customer.data.unittest;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.request.CustomerUpdateRequest;
import com.customer.data.response.CustomerResponse;
import com.customer.data.service.CustomerService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @Mock
    private CustomerRepositoryJpa repository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void getAllCustomersTest(){
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();
        List<Customer> customerList = List.of(customer);

        when(repository.findAll()).thenReturn(customerList);

        // when -  action or the behaviour that we are going test
        List<CustomerResponse> result = customerService.getAll();

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.get(0).getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.get(0).getAge()).isEqualTo(27);
    }

    @Test
    public void addCustomerWithoutAddressTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();

        when(repository.save(any(Customer.class))).thenReturn(customer);

        // when -  action or the behaviour that we are going test
        CustomerResponse result = customerService.addCustomer(customerRequest);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(27);
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void addCustomerWithoutEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "", requestDate, addressRequest);

        Address address = Address.builder().country("Rom").city("Iasi").street("Musatini").houseNumber("5").postalCode("440077").build();

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("").age(date).build();
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);

        // when -  action or the behaviour that we are going test
        CustomerResponse result = customerService.addCustomer(customerRequest);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo("");
        assertThat(result.getAge()).isEqualTo(27);
        assertThat(result.getCurrentLivingAddress()).isNotNull();
        assertThat(result.getCurrentLivingAddress().getCountry()).isEqualTo(address.getCountry());
        assertThat(result.getCurrentLivingAddress().getCity()).isEqualTo(address.getCity());
        assertThat(result.getCurrentLivingAddress().getStreet()).isEqualTo(address.getStreet());
        assertThat(result.getCurrentLivingAddress().getHouseNumber()).isEqualTo(address.getHouseNumber());
        assertThat(result.getCurrentLivingAddress().getPostalCode()).isEqualTo(address.getPostalCode());
    }

    @Test
    public void addCustomersWithSameAddressTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("David").lastName("MMM").email("david@yahoo.com").age(date).build();
        List<Customer> customerList = List.of(customer);

        when(repository.findByEmail(any(String.class))).thenReturn(customerList);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("This email already exists!", exception.getMessage());
    }

    @Test
    public void addCustomersWithAgeBelow18Test() throws CustomerValidationException {
        // given - precondition or setup
        String requestDate = "2015-04-16";
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("Customer age is smaller than 18!", exception.getMessage());
    }

    @Test
    public void getCustomerByIdTest() throws CustomerValidationException {
        // given - precondition or setup

        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        CustomerResponse result = customerService.getCustomerById(1L);

        // then - verify the output
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(27);
    }

    @Test
    public void getCustomerByFirstNameTest(){
        // given - precondition or setup

        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();
        List<Customer> customerList = List.of(customer);
        String firstName = "Gabi";

        when(repository.findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(any(String.class), any(String.class))).thenReturn(customerList);

        // when -  action or the behaviour that we are going test
        List<CustomerResponse> result = customerService.getCustomerByName(firstName);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.get(0).getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.get(0).getAge()).isEqualTo(27);
    }

    @Test
    public void updateCustomerWithoutAddressTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("gabi@yahoo.com", null);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        CustomerResponse result = customerService.updateCustomer(customerRequest, 1L);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(27);
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void updateCustomerWithoutEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("", addressRequest);

        Address address = Address.builder().country("Rom").city("Iasi").street("Musatini").houseNumber("5").postalCode("440077").build();
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("").age(date).build();
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        CustomerResponse result = customerService.updateCustomer(customerRequest, 1L);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isNull();
        assertThat(result.getAge()).isEqualTo(27);
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
        LocalDate date = LocalDate.of(1997, 1, 2);
        AddressRequest addressRequest = null;
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("", addressRequest);

        Address address = Address.builder().country("Rom").city("Iasi").street("Musatini").houseNumber("5").postalCode("440077").build();
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").age(date).email(null).currentLivingAddress(null).build();
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.updateCustomer(customerRequest, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isNull();
        assertThat(result.getAge()).isEqualTo(27);
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void updateCustomerEmptyLastNameTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        AddressRequest addressRequest = null;
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("gabi@yahoo.com", addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("").age(date).build();

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        CustomerResponse result = customerService.updateCustomer(customerRequest, 1L);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(27);
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void updateNotExistingCustomerWithIdTest() throws CustomerValidationException {
        // given - precondition or setup
        AddressRequest addressRequest = new AddressRequest();
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("gabi@yahoo.com", addressRequest);

        when(repository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.updateCustomer(customerRequest, 1L);
        });

        assertEquals("The customer with id 1 doesn't exist", exception.getMessage());
    }

}
