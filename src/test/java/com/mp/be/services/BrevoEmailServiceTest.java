package com.mp.be.services;

import com.mp.be.config.BrevoConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrevoEmailServiceTest {

    @InjectMocks
    private BrevoEmailService brevoEmailService;

    @Mock
    private BrevoConfig brevoConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail() {
        when(brevoConfig.getEmailFrom()).thenReturn("noreply@example.com");
        when(brevoConfig.getApiKey()).thenReturn("dummyApiKey");

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "Test User");

        brevoEmailService.sendEmail("test@example.com", "1", variables);
    }

    @Test
    void testCreateHeaders() throws Exception {
        when(brevoConfig.getApiKey()).thenReturn("dummyApiKey");

        // Use reflection to access the private method
        java.lang.reflect.Method method = BrevoEmailService.class.getDeclaredMethod("createHeaders");
        method.setAccessible(true);
        HttpHeaders headers = (HttpHeaders) method.invoke(brevoEmailService);

        assertNotNull(headers);
        assertEquals("application/json", headers.getContentType().toString());
        assertEquals("dummyApiKey", headers.getFirst("api-key"));
    }
} 