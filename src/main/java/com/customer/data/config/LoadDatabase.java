package com.customer.data.config;

import ch.qos.logback.classic.Logger;
import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.repository.AddressRepositoryJpa;
import com.customer.data.repository.CustomerRepositoryJpa;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
class LoadDatabase {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CustomerRepositoryJpa customerRepositoryJpa, AddressRepositoryJpa addressRepositoryJpa) {

        return args -> {
            Customer c1 = new Customer("Gabi", "Ab", "gabi@yahoo.com", 27);
            Address a1 = new Address("Rom", "Iasi", "Musatini", "5", "440077");
            c1.setCurrentLivingAddress(a1);

            Customer c2 = new Customer("Dani", "Ab", "gabi@yahoo.com", 27);
            Address a2 = new Address("Rom", "Iasi", "Musatini", "5", "440077");
            c2.setCurrentLivingAddress(a2);

            Customer c3 = new Customer("Andrei", "Ab", "gabi@yahoo.com", 27);
            Address a3 = new Address("Rom", "Iasi", "Musatini", "5", "440077");
            c3.setCurrentLivingAddress(a3);

            addressRepositoryJpa.save(a1);
            addressRepositoryJpa.save(a2);
            addressRepositoryJpa.save(a3);
            a1.setCustomer(c1);
            a2.setCustomer(c2);
            a3.setCustomer(c3);
            log.info("Preloading " + customerRepositoryJpa.save(c1));
            log.info("Preloading " + customerRepositoryJpa.save(c2));
            log.info("Preloading " + customerRepositoryJpa.save(c3));
        };
    }
}
