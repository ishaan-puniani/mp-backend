package com.mp.be.models.tenant;

import com.mp.be.models.settings.SettingsModel;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TenantModelTest {

    @Test
    public void testTenantModelGettersAndSetters() {
        TenantModel tenant = new TenantModel();

        tenant.setId("test-id");
        assertEquals("test-id", tenant.getId());

        tenant.setName("Test Tenant");
        assertEquals("Test Tenant", tenant.getName());

        tenant.setUrl("http://test.com");
        assertEquals("http://test.com", tenant.getUrl());

        tenant.setPlan("Basic");
        assertEquals("Basic", tenant.getPlan());

        tenant.setPlanStatus("Active");
        assertEquals("Active", tenant.getPlanStatus());

        tenant.setCreatedBy("creator");
        assertEquals("creator", tenant.getCreatedBy());

        tenant.setUpdatedBy("updater");
        assertEquals("updater", tenant.getUpdatedBy());

        Date createdAt = new Date();
        tenant.setCreatedAt(createdAt);
        assertEquals(createdAt, tenant.getCreatedAt());

        Date updatedAt = new Date();
        tenant.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, tenant.getUpdatedAt());

        SettingsModel settings = new SettingsModel();
        tenant.setSettings(settings);
        assertEquals(settings, tenant.getSettings());
    }
}