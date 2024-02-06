package com.customer.data.unittest;

import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.exception.CustomerValidationException;
import com.customer.data.repository.CustomerRepositoryJpa;
import com.customer.data.request.AddressRequest;
import com.customer.data.request.CustomerRequest;
import com.customer.data.request.CustomerUpdateRequest;
import com.customer.data.service.CustomerService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        List<Customer> customerList = Arrays.asList(customer);

        when(repository.findAll()).thenReturn(customerList);

        // when -  action or the behaviour that we are going test
        List<Customer> result = customerService.getAll();

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
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();

        when(repository.save(any(Customer.class))).thenReturn(customer);

        // when -  action or the behaviour that we are going test
        Customer result = customerService.addCustomer(customerRequest);

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
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "", requestDate, addressRequest);

        Address address = Address.builder().country("Rom").city("Iasi").street("Musatini").houseNumber("5").postalCode("440077").build();

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("").age(date).build();
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);

        // when -  action or the behaviour that we are going test
        Customer result = customerService.addCustomer(customerRequest);

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

    /*@Test
    public void addCustomerEmptyAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = null;
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "", requestDate, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer(1L, "Gabi", "Abrudan", "", date);
        customer.setCurrentLivingAddress(address);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("Both email and address are empty!", exception.getMessage());
    }*/

    /*@Test
    public void addCustomerEmptyCountryAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("", "Iasi", "Musatini", "5", "440077");
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "", requestDate, addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer(1L, "Gabi", "Abrudan", "", date);
        customer.setCurrentLivingAddress(address);

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.addCustomer(customerRequest);
        });

        assertEquals("Customer country address is empty!", exception.getMessage());
    }*/

//    @Test
//    public void addCustomerEmptyFirstNameTest() throws CustomerValidationException {
//        // given - precondition or setup
//        LocalDate date = LocalDate.of(1997, 1, 2);
//        String requestDate = "1997-01-02";
//        AddressRequest addressRequest = null;
//        CustomerRequest customerRequest = new CustomerRequest("", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
//
//        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
//        Customer customer = new Customer(1L, "Gabi", "Abrudan", "", date);
//        customer.setCurrentLivingAddress(address);
//
//        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
//            customerService.addCustomer(customerRequest);
//        });
//
//        assertEquals("Customer first name is empty!", exception.getMessage());
//    }

//    @Test
//    public void addAlreadyExistingCustomerWithIdTest() throws CustomerValidationException {
//        // given - precondition or setup
//        LocalDate date = LocalDate.of(1997, 1, 2);
//        LocalDate date2 = LocalDate.of(1998, 2, 4);
//        String requestDate = "1997-01-02";
//        String requestDate2 = "1998-02-04";
//        AddressRequest addressRequest = new AddressRequest();
//        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);
//        CustomerRequest customerRequest2 = new CustomerRequest("Dani", "Bala", "dani@yahoo.com", requestDate2, addressRequest);
//
//        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();
//
//        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));
//
//        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
//            customerService.addCustomer(customerRequest2);
//        });
//
//        assertEquals("The customer with id 1 already exists", exception.getMessage());
//    }

    @Test
    public void addCustomersWithSameAddressTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest();
        CustomerRequest customerRequest = new CustomerRequest("Gabi", "Abrudan", "gabi@yahoo.com", requestDate, addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("David").lastName("MMM").email("david@yahoo.com").age(date).build();
        List<Customer> customerList = Arrays.asList(customer);

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

        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();
        List<Customer> customerList = Arrays.asList(customer);
        String firstName = "Gabi";

        when(repository.findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(any(String.class), any(String.class))).thenReturn(customerList);

        // when -  action or the behaviour that we are going test
        List<Customer> result = customerService.getCustomerByName(firstName);

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
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest();
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("gabi@yahoo.com", null);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.updateCustomer(customerRequest, 1L);

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
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "Iasi", "Musatini", "5", "440077");
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("", addressRequest);

        Address address = Address.builder().country("Rom").city("Iasi").street("Musatini").houseNumber("5").postalCode("440077").build();
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("").age(date).build();
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.updateCustomer(customerRequest, 1L);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isNull();
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
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = null;
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("", addressRequest);

        Address address = Address.builder().country("Rom").city("Iasi").street("Musatini").houseNumber("5").postalCode("440077").build();
        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email(null).currentLivingAddress(null).build();
        customer.setCurrentLivingAddress(address);

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        Customer result = customerService.updateCustomer(customerRequest, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isNull();
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNull();

//        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));
//
//        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
//            customerService.updateCustomer(customerRequest, 1L);
//        });
//
//        assertEquals("Both email and address are empty!", exception.getMessage());
    }

    /*@Test
    public void updateCustomerEmptyCityAddressAndEmailTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest("Rom", "", "Musatini", "5", "440077");
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("", addressRequest);

        Address address = new Address("Rom", "Iasi", "Musatini", "5", "440077");
        Customer customer = new Customer(1L, "Gabi", "Abrudan", "", date);
        customer.setCurrentLivingAddress(address);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.updateCustomer(customerRequest, 1L);
        });

        assertEquals("Customer city address is empty!", exception.getMessage());
    }*/

    @Test
    public void updateCustomerEmptyLastNameTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = null;
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("gabi@yahoo.com", addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("").age(date).build();

        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        // when -  action or the behaviour that we are going test
        Customer result = customerService.updateCustomer(customerRequest, 1L);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        assertThat(result.getAge()).isEqualTo(customer.getAge());
        assertThat(result.getCurrentLivingAddress()).isNull();
    }

    @Test
    public void updateNotExistingCustomerWithIdTest() throws CustomerValidationException {
        // given - precondition or setup
        LocalDate date = LocalDate.of(1997, 1, 2);
        String requestDate = "1997-01-02";
        AddressRequest addressRequest = new AddressRequest();
        CustomerUpdateRequest customerRequest = new CustomerUpdateRequest("gabi@yahoo.com", addressRequest);

        Customer customer = Customer.builder().id(1L).firstName("Gabi").lastName("Abrudan").email("gabi@yahoo.com").age(date).build();

        when(repository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));

        Throwable exception = assertThrows(CustomerValidationException.class, () -> {
            customerService.updateCustomer(customerRequest, 1L);
        });

        assertEquals("The customer with id 1 doesn't exist", exception.getMessage());
    }

}
