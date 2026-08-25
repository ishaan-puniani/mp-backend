package com.mp.be.services.customer;

import com.mp.be.database.entities.Customer;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.CustomerRepository;
import com.mp.be.models.customer.CustomerRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.customer.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        CustomerRequestModel requestModel = new CustomerRequestModel();
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        Query query = new Query(Criteria.where("tenant").is("tenantId"));
        when(mongoTemplate.count(any(Query.class), eq(Customer.class))).thenReturn(0L);
        when(mongoTemplate.find(any(Query.class), eq(Customer.class))).thenReturn(Collections.emptyList());

        // Act
        Page<Customer> result = customerServiceImpl.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Customer.class));
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Customer.class));
    }

    @Test
    void testFindAll() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Customer> result = customerServiceImpl.findAll(mock(ServiceOptions.class));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testFind() {
        // Arrange
        String customerId = "123";
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerServiceImpl.find(mock(ServiceOptions.class), customerId);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testCreate() {
        // Arrange
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer result = customerServiceImpl.create(mock(ServiceOptions.class), customer);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testImportData() {
        // Arrange
        Customer customer = new Customer();
        String importHash = "uniqueHash";
        when(customerRepository.countImportHash(importHash)).thenReturn(0L);
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer result = customerServiceImpl.importData(mock(ServiceOptions.class), customer, importHash);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testDelete() {
        // Arrange
        String customerId = "123";
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        customerServiceImpl.delete(mock(ServiceOptions.class), customerId);

        // Assert
        verify(customerRepository, times(1)).delete(customer);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        // Arrange
        String customerId = "123";
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer result = customerServiceImpl.update(mock(ServiceOptions.class), customerId, customer);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
        verify(auditLogRepository, times(1)).save(any());
    }
} 