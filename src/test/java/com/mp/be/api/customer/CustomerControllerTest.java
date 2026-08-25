package com.mp.be.api.customer;

import com.mp.be.database.entities.Customer;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.customer.CustomerDataModel;
import com.mp.be.models.customer.CustomerRequestModel;
import com.mp.be.models.generic.ImportRequestModel;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.customer.CustomerService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        Customer customer = new Customer();
        Page<Customer> page = new PageImpl<>(Collections.singletonList(customer));

        // Success scenario
        when(customerService.findAndCountAll(any(ServiceOptions.class), any(CustomerRequestModel.class), any(), any(), any()))
                .thenReturn(page);
        ResponseEntity<ListResponseModel<Customer>> response = customerController.findAndCountAll(request, new CustomerRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        assertEquals(200, response.getStatusCodeValue());
        ListResponseModel<Customer> responseBody = response.getBody();
        assertEquals(1, responseBody.rows.size());

        // Failure scenario
        when(customerService.findAndCountAll(any(ServiceOptions.class), any(CustomerRequestModel.class), any(), any(), any()))
                .thenThrow(new RuntimeException("Listing failed"));
        try {
            response = customerController.findAndCountAll(request, new CustomerRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        } catch (RuntimeException e) {
            assertEquals("Listing failed", e.getMessage());
        }
    }

    @Test
    void testFind() {
        String customerId = "1";
        Customer customer = new Customer();

        // Success scenario
        when(customerService.find(any(ServiceOptions.class), eq(customerId))).thenReturn(customer);
        Customer response = customerController.find(request, customerId);
        assertEquals(customer, response);

        // Failure scenario
        when(customerService.find(any(ServiceOptions.class), eq(customerId))).thenThrow(new RuntimeException("Customer not found"));
        try {
            response = customerController.find(request, customerId);
        } catch (RuntimeException e) {
            assertEquals("Customer not found", e.getMessage());
        }
    }

    @Test
    void testCreate() {
        Customer customer = new Customer();
        CustomerDataModel customerDataModel = new CustomerDataModel();
        customerDataModel.data = customer;

        // Success scenario
        when(customerService.create(any(ServiceOptions.class), any(Customer.class))).thenReturn(customer);
        Customer response = customerController.create(request, customerDataModel);
        assertEquals(customer, response);

        // Failure scenario
        when(customerService.create(any(ServiceOptions.class), any(Customer.class))).thenThrow(new RuntimeException("Creation failed"));
        try {
            response = customerController.create(request, customerDataModel);
        } catch (RuntimeException e) {
            assertEquals("Creation failed", e.getMessage());
        }
    }

    @Test
    void testUpdate() {
        String customerId = "1";
        Customer customer = new Customer();
        CustomerDataModel customerDataModel = new CustomerDataModel();
        customerDataModel.data = customer;

        // Success scenario
        when(customerService.update(any(ServiceOptions.class), eq(customerId), any(Customer.class))).thenReturn(customer);
        Customer response = customerController.update(request, customerId, customerDataModel);
        assertEquals(customer, response);

        // Failure scenario
        when(customerService.update(any(ServiceOptions.class), eq(customerId), any(Customer.class))).thenThrow(new RuntimeException("Update failed"));
        try {
            response = customerController.update(request, customerId, customerDataModel);
        } catch (RuntimeException e) {
            assertEquals("Update failed", e.getMessage());
        }
    }

    @Test
    void testDelete() {
        List<String> ids = List.of("1", "2");

        // Success scenario
        doNothing().when(customerService).delete(any(ServiceOptions.class), any(String.class));
        customerController.delete(request, ids);

        // Failure scenario
        doThrow(new RuntimeException("Deletion failed")).when(customerService).delete(any(ServiceOptions.class), any(String.class));
        try {
            customerController.delete(request, ids);
        } catch (RuntimeException e) {
            assertEquals("Deletion failed", e.getMessage());
        }
    }

    @Test
    void testImportData() {
        Customer customer = new Customer();
        ImportRequestModel<Customer> importRequestModel = new ImportRequestModel<>();
        importRequestModel.setData(customer);

        // Success scenario
        when(customerService.importData(any(ServiceOptions.class), any(Customer.class), any(String.class))).thenReturn(customer);
        boolean result = customerController.importData(request, importRequestModel);
        assertEquals(true, result);

        // Failure scenario
        when(customerService.importData(any(ServiceOptions.class), any(Customer.class), any(String.class))).thenThrow(new RuntimeException("Import failed"));
        try {
            result = customerController.importData(request, importRequestModel);
        } catch (RuntimeException e) {
            assertEquals("Import failed", e.getMessage());
        }
    }

    @Test
    void testAutocomplete() {
        when(customerService.findAll(any(ServiceOptions.class))).thenReturn(Collections.singletonList(new Customer()));
        List<AutoComplete> response = customerController.autocomplete(request, "query", 10);
        assertEquals(1, response.size());

        // Failure scenario
        when(customerService.findAll(any(ServiceOptions.class))).thenThrow(new RuntimeException("Autocomplete failed"));
        try {
            response = customerController.autocomplete(request, "query", 10);
        } catch (RuntimeException e) {
            assertEquals("Autocomplete failed", e.getMessage());
        }
    }

} 