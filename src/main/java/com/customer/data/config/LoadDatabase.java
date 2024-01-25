package com.customer.data.config;

import ch.qos.logback.classic.Logger;
import com.customer.data.entity.Address;
import com.customer.data.entity.Customer;
import com.customer.data.repository.CustomerRepositoryJpa;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
class LoadDatabase {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CustomerRepositoryJpa customerRepositoryJpa) {

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

            Customer c4 = new Customer("Mircea", "Ab", "gabi@yahoo.com", 27);

            log.info("Preloading " + customerRepositoryJpa.save(c1));
            log.info("Preloading " + customerRepositoryJpa.save(c2));
            log.info("Preloading " + customerRepositoryJpa.save(c3));
            log.info("Preloading " + customerRepositoryJpa.save(c4));
        };
    }
}
