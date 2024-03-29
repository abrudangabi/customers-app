package com.customer.data.repository;

import com.customer.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepositoryJpa extends JpaRepository<Customer, Long> {
    Optional<Customer> findById(Long id);
    List<Customer> findByEmail(String email);
    List<Customer> findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(String firstName, String lastName);
}
