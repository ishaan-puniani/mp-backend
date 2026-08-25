package com.mp.be.services.order;

import com.mp.be.database.entities.Order;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.OrderRepository;
import com.mp.be.models.order.OrderRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        OrderRequestModel requestModel = new OrderRequestModel();
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        Query query = new Query();
        when(mongoTemplate.count(any(Query.class), eq(Order.class))).thenReturn(0L);
        when(mongoTemplate.find(any(Query.class), eq(Order.class))).thenReturn(Collections.emptyList());

        // Act
        Page<Order> result = orderServiceImpl.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Order.class));
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Order.class));
    }

    @Test
    void testFindAll() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Order> result = orderServiceImpl.findAll(mock(ServiceOptions.class));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFind() {
        // Arrange
        String orderId = "123";
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderServiceImpl.find(mock(ServiceOptions.class), orderId);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testCreate() {
        // Arrange
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order result = orderServiceImpl.create(mock(ServiceOptions.class), order);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testImportData() {
        // Arrange
        Order order = new Order();
        String importHash = "uniqueHash";
        when(orderRepository.countImportHash(importHash)).thenReturn(0L);
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order result = orderServiceImpl.importData(mock(ServiceOptions.class), order, importHash);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testDelete() {
        // Arrange
        String orderId = "123";
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        orderServiceImpl.delete(mock(ServiceOptions.class), orderId);

        // Assert
        verify(orderRepository, times(1)).delete(order);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        // Arrange
        String orderId = "123";
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order result = orderServiceImpl.update(mock(ServiceOptions.class), orderId, order);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(auditLogRepository, times(1)).save(any());
    }
} 