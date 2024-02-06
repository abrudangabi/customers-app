package com.customer.data.config;

import ch.qos.logback.classic.Logger;
import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.repository.CustomerRepositoryJpa;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;


@Configuration
class LoadDatabase {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LoadDatabase.class);

    // todo: activate Bean for local testing
//    @Bean
    CommandLineRunner initDatabase(CustomerRepositoryJpa customerRepositoryJpa) {

        return args -> {
            LocalDate date = LocalDate.of(1997, 1, 2);
            Customer c1 = new Customer(1L, "Gabi", "Ab", "gabi@yahoo.com", date);
            Address a1 = new Address("Rom", "Iasi", "Musatini", "5", "440077");
            c1.setCurrentLivingAddress(a1);

            Customer c2 = new Customer(2L, "Dani", "Ab", "dani@yahoo.com", date);
            Address a2 = new Address("Rom", "Iasi", "Musatini", "5", "440077");
            c2.setCurrentLivingAddress(a2);

            Customer c3 = new Customer(3L, "Andrei", "Ab", "andrei@yahoo.com", date);
            Address a3 = new Address("Rom", "Iasi", "Musatini", "5", "440077");
            c3.setCurrentLivingAddress(a3);

            Customer c4 = new Customer(4L, "Mircea", "Ab", "mircea@yahoo.com", date);

            log.info("Preloading " + customerRepositoryJpa.save(c1));
            log.info("Preloading " + customerRepositoryJpa.save(c2));
            log.info("Preloading " + customerRepositoryJpa.save(c3));
            log.info("Preloading " + customerRepositoryJpa.save(c4));
        };
    }
}
