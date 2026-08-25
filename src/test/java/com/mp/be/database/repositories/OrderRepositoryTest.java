package com.mp.be.database.repositories;

import com.mp.be.database.entities.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveAndFindById() {
        Order order = new Order();
        orderRepository.save(order);

        Order found = orderRepository.findById(order.getId()).orElse(null);
        assertThat(found).isNotNull();
    }

    @Test
    void testDelete() {
        Order order = new Order();
        orderRepository.save(order);
        orderRepository.delete(order);

        Order found = orderRepository.findById(order.getId()).orElse(null);
        assertThat(found).isNull();
    }
} 