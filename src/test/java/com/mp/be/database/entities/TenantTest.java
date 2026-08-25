package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TenantTest {

    @Test
    public void testTenantConstructorAndFields() {
        Setting setting = new Setting();
        Tenant tenant = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        assertEquals("Tenant1", tenant.getName());
        assertEquals("http://tenant1.com", tenant.getUrl());
        assertEquals("Basic", tenant.getPlan());
        assertEquals("Active", tenant.getPlanStatus());
        assertEquals("stripe123", tenant.getPlanStripeCustomerId());
        assertEquals("user123", tenant.getPlanUserId());
        assertEquals(setting, tenant.getSettings());
    }

    @Test
    public void testLombokGeneratedMethods() {
        Tenant tenant = new Tenant();
        tenant.setName("Tenant2");
        assertEquals("Tenant2", tenant.getName());
    }

    @Test
    public void testBoundaryConditions() {
        Tenant tenant = new Tenant();
        tenant.setName("");
        assertEquals("", tenant.getName());
    }

    @Test
    public void testNullAndInvalidValues() {
        Tenant tenant = new Tenant();
        tenant.setName(null);
        assertNull(tenant.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        assertEquals(tenant1, tenant2);
        assertEquals(tenant1.hashCode(), tenant2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentFields() {
        Setting setting1 = new Setting();
        Setting setting2 = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting1);
        Tenant tenant2 = new Tenant("Tenant2", "http://tenant2.com", "Premium", "Inactive", "stripe456", "user456", setting2);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithNullFields() {
        Tenant tenant1 = new Tenant(null, null, null, null, null, null, null);
        Tenant tenant2 = new Tenant(null, null, null, null, null, null, null);
        assertEquals(tenant1, tenant2);
    }

    @Test
    public void testHashCodeConsistency() {
        Setting setting = new Setting();
        Tenant tenant = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        int initialHashCode = tenant.hashCode();
        assertEquals(initialHashCode, tenant.hashCode());
        assertEquals(initialHashCode, tenant.hashCode());
    }

    @Test
    public void testToString() {
        Setting setting = new Setting();
        Tenant tenant = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        assertNotNull(tenant.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant2", "http://tenant2.com", "Basic", "Active", "stripe123", "user123", setting);
        assertNotEquals(tenant1, tenant2);
        assertNotEquals(tenant1.hashCode(), tenant2.hashCode());

        assertNotEquals(tenant1, null);
        assertNotEquals(tenant1, new Object());
    }

    @Test
    public void testEqualsWithDifferentName() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant2", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithDifferentUrl() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant2.com", "Basic", "Active", "stripe123", "user123", setting);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithDifferentPlan() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant1.com", "Premium", "Active", "stripe123", "user123", setting);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithDifferentPlanStatus() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Inactive", "stripe123", "user123", setting);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithDifferentPlanStripeCustomerId() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe456", "user123", setting);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithDifferentPlanUserId() {
        Setting setting = new Setting();
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user456", setting);
        assertNotEquals(tenant1, tenant2);
    }

    @Test
    public void testEqualsWithDifferentSettings() {
        Setting setting1 = new Setting();
        setting1.setTheme("dark");
        
        Setting setting2 = new Setting();
        setting2.setTheme("light");
        
        Tenant tenant1 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting1);
        Tenant tenant2 = new Tenant("Tenant1", "http://tenant1.com", "Basic", "Active", "stripe123", "user123", setting2);
        
        assertNotEquals(tenant1, tenant2);
    }
} 