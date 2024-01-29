package com.customer.data.repository;

import com.customer.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepositoryJpa extends JpaRepository<Customer, Long> {
    @Override
    Optional<Customer> findById(Long aLong);
    List<Customer> findByFirstName(String firstName);
    List<Customer> findByLastName(String lastName);
    List<Customer> findByEmail(String email);
}
