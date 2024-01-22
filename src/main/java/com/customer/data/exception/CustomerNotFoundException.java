package com.customer.data.exception;

public class CustomerNotFoundException extends RuntimeException{

    CustomerNotFoundException(Long id) {
        super("Could not find customer " + id);
    }
}
