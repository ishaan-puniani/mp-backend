package com.mp.be.services.auditLog;

import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.models.auditLog.AuditLogRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.auditLog.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertEquals(expectedPage.getContent(), result.getContent(), "The content of the pages should be equal");
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements(), "The total elements should be equal");
        assertEquals(expectedPage.getTotalPages(), result.getTotalPages(), "The total pages should be equal");
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFind() {
        // Arrange
        String id = "123";
        AuditLog auditLog = new AuditLog();
        when(repository.findById(id)).thenReturn(Optional.of(auditLog));

        // Act
        AuditLog result = auditLogService.find(mock(ServiceOptions.class), id);

        // Assert
        assertEquals(auditLog, result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testCreate() {
        // Arrange
        AuditLog auditLog = new AuditLog();
        when(repository.save(auditLog)).thenReturn(auditLog);

        // Act
        AuditLog result = auditLogService.create(mock(ServiceOptions.class), auditLog);

        // Assert
        assertEquals(auditLog, result);
        verify(repository, times(1)).save(auditLog);
    }

    @Test
    void testDelete() {
        // Arrange
        String id = "123";

        // Act
        auditLogService.delete(mock(ServiceOptions.class), id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindAndCountAllWithException() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");
        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            auditLogService.findAndCountAll(serviceOptions, new AuditLogRequestModel(), Optional.of(10), Optional.of(0), Optional.empty());
        });
    }

    @Test
    void testFindWithNonExistentId() {
        // Arrange
        String id = "nonExistentId";
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        AuditLog result = auditLogService.find(mock(ServiceOptions.class), id);

        // Assert
        assertNull(result);
    }

    @Test
    void testFindAndCountAllWithBoundaryValues() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");
        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        PageRequest pageRequest = PageRequest.of(0, 1000);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(Collections.emptyList());
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(0L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(Integer.MAX_VALUE), Optional.of(Integer.MAX_VALUE), Optional.empty());

        // Assert
        assertTrue(result.isEmpty(), "The result should be empty");
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithFilters() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        requestModel.setFilter(Map.of("entityId", "123", "action", "create"));
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertEquals(expectedPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithOrderBy() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.of("timestamp"));

        // Assert
        assertEquals(expectedPage.getContent(), result.getContent(), "The content of the pages should be equal");
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements(), "The total elements should be equal");
        assertEquals(expectedPage.getTotalPages(), result.getTotalPages(), "The total pages should be equal");
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testCreateWithInvalidData() {
        // Arrange
        AuditLog auditLog = new AuditLog();
        // Assuming there's a validation that throws an exception for invalid data
        when(repository.save(auditLog)).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            auditLogService.create(mock(ServiceOptions.class), auditLog);
        });
    }

    @Test
    void testDeleteNonExistentId() {
        // Arrange
        String id = "nonExistentId";
        doThrow(new IllegalArgumentException("ID not found")).when(repository).deleteById(id);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            auditLogService.delete(mock(ServiceOptions.class), id);
        });
    }

    @Test
    void testFindAndCountAllWithAllFilters() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        requestModel.setFilter(Map.of(
            "entityId", "123",
            "action", "create",
            "createdByEmail", "test@example.com",
            "entityNames", List.of("entity1", "entity2"),
            "timestampRange", List.of("2023-01-01T00:00:00Z", "2023-12-31T23:59:59Z")
        ));
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertEquals(expectedPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithDifferentOrderBy() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.of("action"));

        // Assert
        assertEquals(expectedPage.getContent(), result.getContent(), "The content of the pages should be equal");
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements(), "The total elements should be equal");
        assertEquals(expectedPage.getTotalPages(), result.getTotalPages(), "The total pages should be equal");
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithDifferentLimitsAndOffsets() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        PageRequest pageRequest = PageRequest.of(1, 5);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(5), Optional.of(5), Optional.empty());

        // Assert
        assertEquals(expectedPage.getContent(), result.getContent(), "The content of the pages should be equal");
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements(), "The total elements should be equal");
        assertEquals(expectedPage.getTotalPages(), result.getTotalPages(), "The total pages should be equal");
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithCreatedByEmail() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        requestModel.setFilter(Map.of("createdByEmail", "test@example.com"));
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertEquals(expectedPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithEntityNames() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        requestModel.setFilter(Map.of("entityNames", List.of("entity1", "entity2")));
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertEquals(expectedPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }

    @Test
    void testFindAndCountAllWithTimestampRange() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        AuditLogRequestModel requestModel = new AuditLogRequestModel();
        requestModel.setFilter(Map.of("timestampRange", List.of("2023-01-01T00:00:00Z", "2023-12-31T23:59:59Z")));
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AuditLog> auditLogs = Collections.singletonList(new AuditLog());
        Page<AuditLog> expectedPage = new PageImpl<>(auditLogs, pageRequest, 1);

        when(mongoTemplate.find(any(Query.class), eq(AuditLog.class))).thenReturn(auditLogs);
        when(mongoTemplate.count(any(Query.class), eq(AuditLog.class))).thenReturn(1L);

        // Act
        Page<AuditLog> result = auditLogService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertEquals(expectedPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(AuditLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(AuditLog.class));
    }
} 