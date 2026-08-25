package com.mp.be.interceptor;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.repositories.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TenantInterceptorTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private TenantInterceptor tenantInterceptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPreHandleWithValidTenantPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/tenant/123/entity");
        when(tenantRepository.findById("123")).thenReturn(java.util.Optional.of(new Tenant()));

        boolean result = tenantInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(request).setAttribute(eq("currentTenant"), any(Tenant.class));
    }
} 