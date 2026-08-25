package com.mp.be.api.tenant;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.User;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.tenant.TenantDataModel;
import com.mp.be.models.tenant.TenantRequestModel;
import com.mp.be.services.tenant.TenantService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TenantControllerTest {

    @Mock
    private TenantService tenantService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TenantController tenantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTenant() {
        Tenant tenant = new Tenant();
        TenantDataModel tenantDataModel = new TenantDataModel();
        tenantDataModel.data = tenant;

        // Success scenario
        when(tenantService.create(any(Tenant.class), any())).thenReturn(tenant);
        ResponseEntity<Tenant> response = tenantController.createTenant(tenantDataModel, request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tenant, response.getBody());

        // Failure scenario
        when(tenantService.create(any(Tenant.class), any())).thenThrow(new RuntimeException("Creation failed"));
        try {
            response = tenantController.createTenant(tenantDataModel, request);
        } catch (RuntimeException e) {
            assertEquals("Creation failed", e.getMessage());
        }
    }

    @Test
    void testListTenants() {
        Tenant tenant1 = new Tenant();
        Tenant tenant2 = new Tenant();
        List<Tenant> tenants = List.of(tenant1, tenant2);
        Page<Tenant> page = new PageImpl<>(tenants);

        // Success scenario
        when(tenantService.findAndCountAll(any(ServiceOptions.class), any(TenantRequestModel.class), any(), any(), any()))
                .thenReturn(page);
        ResponseEntity<ListResponseModel<Tenant>> response = tenantController.list(request, new TenantRequestModel(), Optional.empty(), Optional.empty(), Optional.empty());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tenants, response.getBody().rows);

        // Failure scenario
        when(tenantService.findAndCountAll(any(ServiceOptions.class), any(TenantRequestModel.class), any(), any(), any()))
                .thenThrow(new RuntimeException("Listing failed"));
        try {
            response = tenantController.list(request, new TenantRequestModel(), Optional.empty(), Optional.empty(), Optional.empty());
        } catch (RuntimeException e) {
            assertEquals("Listing failed", e.getMessage());
        }
    }

    @Test
    void testFindTenant() {
        String tenantId = "1";
        Tenant tenant = new Tenant();

        // Success scenario
        when(tenantService.find(tenantId)).thenReturn(tenant);
        Tenant response = tenantController.find(tenantId);
        assertEquals(tenant, response);

        // Failure scenario
        when(tenantService.find(tenantId)).thenThrow(new RuntimeException("Tenant not found"));
        try {
            response = tenantController.find(tenantId);
        } catch (RuntimeException e) {
            assertEquals("Tenant not found", e.getMessage());
        }
    }

    @Test
    void testAcceptInvitation() {
        String token = "sampleToken";
        Map<String, Boolean> payload = Map.of("forceAcceptOtherEmail", true);
        Tenant tenant = new Tenant();
        tenant.setName("Test Tenant");

        User currentUser = new User();
        when(request.getAttribute("currentUser")).thenReturn(currentUser);

        // Success scenario
        when(tenantService.acceptInvitation(eq(token), eq(currentUser), any(ServiceOptions.class))).thenReturn(tenant);
        ResponseEntity<Tenant> response = tenantController.acceptInvitation(token, payload, request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tenant, response.getBody());

        // Failure scenario
        when(tenantService.acceptInvitation(eq(token), eq(currentUser), any(ServiceOptions.class))).thenThrow(new RuntimeException("Invitation acceptance failed"));
        try {
            response = tenantController.acceptInvitation(token, payload, request);
        } catch (RuntimeException e) {
            assertEquals("Invitation acceptance failed", e.getMessage());
        }
    }

    
} 