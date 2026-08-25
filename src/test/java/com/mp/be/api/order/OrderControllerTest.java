package com.mp.be.api.order;

import com.mp.be.database.entities.Order;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.generic.ImportRequestModel;
import com.mp.be.models.order.OrderDataModel;
import com.mp.be.models.order.OrderRequestModel;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.order.OrderService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        Order order = new Order();
        Page<Order> page = new PageImpl<>(Collections.singletonList(order));

        // Success scenario
        when(orderService.findAndCountAll(any(ServiceOptions.class), any(OrderRequestModel.class), any(), any(), any()))
                .thenReturn(page);
        ResponseEntity<ListResponseModel<Order>> response = orderController.findAndCountAll(request, new OrderRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        assertEquals(200, response.getStatusCodeValue());
        ListResponseModel<Order> responseBody = response.getBody();
        assertEquals(1, responseBody.rows.size());

        // Failure scenario
        when(orderService.findAndCountAll(any(ServiceOptions.class), any(OrderRequestModel.class), any(), any(), any()))
                .thenThrow(new RuntimeException("Listing failed"));
        try {
            response = orderController.findAndCountAll(request, new OrderRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        } catch (RuntimeException e) {
            assertEquals("Listing failed", e.getMessage());
        }
    }

    @Test
    void testFind() {
        String orderId = "1";
        Order order = new Order();

        // Success scenario
        when(orderService.find(any(ServiceOptions.class), eq(orderId))).thenReturn(order);
        Order response = orderController.find(request, orderId);
        assertEquals(order, response);

        // Failure scenario
        when(orderService.find(any(ServiceOptions.class), eq(orderId))).thenThrow(new RuntimeException("Order not found"));
        try {
            response = orderController.find(request, orderId);
        } catch (RuntimeException e) {
            assertEquals("Order not found", e.getMessage());
        }
    }

    @Test
    void testCreate() {
        Order order = new Order();
        OrderDataModel orderDataModel = new OrderDataModel();
        orderDataModel.data = order;

        // Success scenario
        when(orderService.create(any(ServiceOptions.class), any(Order.class))).thenReturn(order);
        Order response = orderController.create(request, orderDataModel);
        assertEquals(order, response);

        // Failure scenario
        when(orderService.create(any(ServiceOptions.class), any(Order.class))).thenThrow(new RuntimeException("Creation failed"));
        try {
            response = orderController.create(request, orderDataModel);
        } catch (RuntimeException e) {
            assertEquals("Creation failed", e.getMessage());
        }
    }

    @Test
    void testUpdate() {
        String orderId = "1";
        Order order = new Order();
        OrderDataModel orderDataModel = new OrderDataModel();
        orderDataModel.data = order;

        // Success scenario
        when(orderService.update(any(ServiceOptions.class), eq(orderId), any(Order.class))).thenReturn(order);
        Order response = orderController.update(request, orderId, orderDataModel);
        assertEquals(order, response);

        // Failure scenario
        when(orderService.update(any(ServiceOptions.class), eq(orderId), any(Order.class))).thenThrow(new RuntimeException("Update failed"));
        try {
            response = orderController.update(request, orderId, orderDataModel);
        } catch (RuntimeException e) {
            assertEquals("Update failed", e.getMessage());
        }
    }

    @Test
    void testDelete() {
        List<String> ids = List.of("1", "2");

        // Success scenario
        doNothing().when(orderService).delete(any(ServiceOptions.class), any(String.class));
        orderController.delete(request, ids);

        // Failure scenario
        doThrow(new RuntimeException("Deletion failed")).when(orderService).delete(any(ServiceOptions.class), any(String.class));
        try {
            orderController.delete(request, ids);
        } catch (RuntimeException e) {
            assertEquals("Deletion failed", e.getMessage());
        }
    }

    @Test
    void testImportData() {
        Order order = new Order();
        ImportRequestModel<Order> importRequestModel = new ImportRequestModel<>();
        importRequestModel.setData(order);

        // Success scenario
        when(orderService.importData(any(ServiceOptions.class), any(Order.class), any(String.class))).thenReturn(order);
        boolean result = orderController.importData(request, importRequestModel);
        assertEquals(true, result);

        // Failure scenario
        when(orderService.importData(any(ServiceOptions.class), any(Order.class), any(String.class))).thenThrow(new RuntimeException("Import failed"));
        try {
            result = orderController.importData(request, importRequestModel);
        } catch (RuntimeException e) {
            assertEquals("Import failed", e.getMessage());
        }
    }

    @Test
    void testAutocomplete() {
        String query = "test";
        Integer limit = 10;
        Order order = new Order();
        order.id = "1";

        // Success scenario
        when(orderService.findAll(any(ServiceOptions.class))).thenReturn(Collections.singletonList(order));
        List<AutoComplete> response = orderController.autocomplete(request, query, limit);
        assertEquals(1, response.size());
        assertEquals(order.id, response.get(0).id);
        assertEquals(order.id, response.get(0).label);

        // Failure scenario
        when(orderService.findAll(any(ServiceOptions.class))).thenThrow(new RuntimeException("Autocomplete failed"));
        try {
            response = orderController.autocomplete(request, query, limit);
        } catch (RuntimeException e) {
            assertEquals("Autocomplete failed", e.getMessage());
        }
    }
} 