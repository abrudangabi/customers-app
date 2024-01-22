package com.customer.data.repository;

import com.customer.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositoryJpa extends JpaRepository<Customer, Integer> {
}
