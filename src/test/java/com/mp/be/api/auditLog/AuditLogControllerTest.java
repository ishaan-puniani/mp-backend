package com.mp.be.api.auditLog;

import com.mp.be.database.entities.AuditLog;
import com.mp.be.models.auditLog.AuditLogRequestModel;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.auditLog.AuditLogService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class AuditLogControllerTest {

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuditLogController auditLogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        AuditLog auditLog = new AuditLog();
        Page<AuditLog> page = new PageImpl<>(Collections.singletonList(auditLog));

        // Test success scenario
        when(auditLogService.findAndCountAll(any(ServiceOptions.class), any(AuditLogRequestModel.class), any(), any(), any()))
                .thenReturn(page);
        ResponseEntity<Object> response = auditLogController.findAndCountAll(request, new AuditLogRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        assertEquals(200, response.getStatusCodeValue());
        ListResponseModel<AuditLog> responseBody = (ListResponseModel<AuditLog>) response.getBody();
        assertEquals(1, responseBody.rows.size());

        // Test failure scenario
        when(auditLogService.findAndCountAll(any(ServiceOptions.class), any(AuditLogRequestModel.class), any(), any(), any()))
                .thenThrow(new RuntimeException("Service error"));
        try {
            response = auditLogController.findAndCountAll(request, new AuditLogRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        } catch (RuntimeException e) {
            assertEquals("Service error", e.getMessage());
        }
    }

    @Test
    void testFind() {
        AuditLog auditLog = new AuditLog();

        // Test success scenario
        when(auditLogService.find(any(ServiceOptions.class), any(String.class))).thenReturn(auditLog);
        AuditLog response = auditLogController.find(request, "1");
        assertEquals(auditLog, response);

        // Test failure scenario
        when(auditLogService.find(any(ServiceOptions.class), any(String.class))).thenThrow(new RuntimeException("Audit log not found"));
        try {
            response = auditLogController.find(request, "1");
        } catch (RuntimeException e) {
            assertEquals("Audit log not found", e.getMessage());
        }
    }

    @Test
    void testCreate() {
        AuditLog auditLog = new AuditLog();

        // Test success scenario
        when(auditLogService.create(any(ServiceOptions.class), any(AuditLog.class))).thenReturn(auditLog);
        AuditLog response = auditLogController.create(request, auditLog);
        assertEquals(auditLog, response);

        // Test failure scenario
        when(auditLogService.create(any(ServiceOptions.class), any(AuditLog.class))).thenThrow(new RuntimeException("Invalid data"));
        try {
            response = auditLogController.create(request, auditLog);
        } catch (RuntimeException e) {
            assertEquals("Invalid data", e.getMessage());
        }
    }

    @Test
    void testDelete() {
        // Test success scenario
        doNothing().when(auditLogService).delete(any(ServiceOptions.class), any(String.class));
        auditLogController.delete(request, "1");

        // Test failure scenario
        doThrow(new RuntimeException("Invalid ID")).when(auditLogService).delete(any(ServiceOptions.class), any(String.class));
        try {
            auditLogController.delete(request, "1");
        } catch (RuntimeException e) {
            assertEquals("Invalid ID", e.getMessage());
        }
    }


} 