package com.customer.data.unittest;

import com.customer.data.entity.Customer;
import com.customer.data.repository.CustomerRepositoryJpa;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepositoryJpa customerRepositoryJpa;

    @Test
    public void saveCustomerTest() {
        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        Customer saveEmployee = customerRepositoryJpa.save(customer);

        assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee.getId()).isGreaterThan(0);

    }

    @Test
    public void findAllCustomersTest() {
        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer employeeOne = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        Customer employeeTwo = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        customerRepositoryJpa.save(employeeOne);
        customerRepositoryJpa.save(employeeTwo);

        List<Customer> employees = customerRepositoryJpa.findAll();

        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    public void findByIdCustomerTest() {
        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        customerRepositoryJpa.save(customer);

        Customer getEmployee = customerRepositoryJpa.findById(customer.getId()).get();

        assertThat(getEmployee).isNotNull();
    }

    @Test
    public void findByEmailCustomerTest() {

        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        customerRepositoryJpa.save(customer);

        Customer getEmployee = customerRepositoryJpa.findByEmail("gabi@yahoo.com").get(0);

        assertThat(getEmployee).isNotNull();
        assertThat(getEmployee.getEmail()).isEqualTo("gabi@yahoo.com");
    }

    @Test
    public void findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCaseTest() {
        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        customerRepositoryJpa.save(customer);

        Customer getEmployee = customerRepositoryJpa.findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase("ga", "ga").get(0);

        assertThat(getEmployee).isNotNull();
        assertThat(getEmployee.getFirstName()).isEqualTo("Gabi");
        assertThat(getEmployee.getLastName()).isEqualTo("Abrudan");
    }

    @Test
    public void updateCustomerTest() {

        LocalDate date = LocalDate.of(1997, 1, 2);
        Customer customer = Customer.builder()
                .firstName("Gabi")
                .lastName("Abrudan")
                .email("gabi@yahoo.com")
                .age(date)
                .build();

        customerRepositoryJpa.save(customer);

        Customer getEmployee = customerRepositoryJpa.findById(customer.getId()).get();

        getEmployee.setFirstName("Name UPDATE");
        getEmployee.setLastName("Last Name");
        getEmployee.setEmail("update@gmail.com");
        getEmployee.setAge(date);

        Customer updatedEmployee = customerRepositoryJpa.save(getEmployee);

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("update@gmail.com");
    }

}
