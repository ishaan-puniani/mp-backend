package com.mp.be.database.repositories;

import com.mp.be.database.entities.Customer;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        Customer customer1 = new Customer("John Doe", "john@example.com");
        customer1.setTenant(String.valueOf(new ObjectId("507f1f77bcf86cd799439011")));
        customerRepository.save(customer1);

        Customer found1 = customerRepository.findById(customer1.getId()).orElse(null);
        assertThat(found1).isNotNull();
        assertThat(found1.getEmail()).isEqualTo("john@example.com");

        Customer customer2 = new Customer("Jane Smith", "jane@example.com");
        customer2.setTenant(String.valueOf(new ObjectId("507f1f77bcf86cd799439014")));
        customerRepository.save(customer2);

        Customer found2 = customerRepository.findById(customer2.getId()).orElse(null);
        assertThat(found2).isNotNull();
        assertThat(found2.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void testDelete() {
        Customer customer = new Customer("Alice Smith", "alice@example.com");
        customer.setTenant(String.valueOf(new ObjectId("507f1f77bcf86cd799439013")));
        customerRepository.save(customer);
        customerRepository.delete(customer);

        Customer found = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(found).isNull();
    }
} 