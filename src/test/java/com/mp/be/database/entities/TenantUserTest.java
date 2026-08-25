package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TenantUserTest {

    @Test
    public void testTenantUserConstructorAndFields() {
        Date now = new Date();
        TenantUser tenantUser = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", now, now);
        assertEquals("1", tenantUser.getId());
        assertEquals(List.of("ROLE_USER"), tenantUser.getRoles());
        assertEquals("tenant1", tenantUser.getTenant());
        assertEquals("active", tenantUser.getStatus());
        assertEquals("token123", tenantUser.getInvitationToken());
        assertEquals(now, tenantUser.getUpdatedAt());
        assertEquals(now, tenantUser.getCreatedAt());
    }

    @Test
    public void testLombokGeneratedMethods() {
        TenantUser tenantUser = new TenantUser();
        tenantUser.setId("2");
        assertEquals("2", tenantUser.getId());
    }

    @Test
    public void testBoundaryConditions() {
        TenantUser tenantUser = new TenantUser();
        tenantUser.setId("");
        assertEquals("", tenantUser.getId());
    }

    @Test
    public void testNullAndInvalidValues() {
        TenantUser tenantUser = new TenantUser();
        tenantUser.setId(null);
        assertNull(tenantUser.getId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date now = new Date();
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", now, now);
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", now, now);
        assertEquals(tenantUser1, tenantUser2);
        assertEquals(tenantUser1.hashCode(), tenantUser2.hashCode());
    }

    @Test
    public void testToString() {
        Date now = new Date();
        TenantUser tenantUser = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", now, now);
        assertNotNull(tenantUser.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        Date now = new Date();
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", now, now);
        TenantUser tenantUser2 = new TenantUser("2", List.of("ROLE_USER"), "tenant1", "active", "token123", now, now);
        assertNotEquals(tenantUser1, tenantUser2);
        assertNotEquals(tenantUser1.hashCode(), tenantUser2.hashCode());

        assertNotEquals(tenantUser1, null);
        assertNotEquals(tenantUser1, new Object());
    }

    @Test
    public void testEqualsWithDifferentId() {
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date());
        TenantUser tenantUser2 = new TenantUser("2", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date());
        assertNotEquals(tenantUser1, tenantUser2);
    }

    @Test
    public void testEqualsWithDifferentRoles() {
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date());
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_ADMIN"), "tenant1", "active", "token123", new Date(), new Date());
        assertNotEquals(tenantUser1, tenantUser2);
    }

    @Test
    public void testEqualsWithDifferentTenant() {
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date());
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_USER"), "tenant2", "active", "token123", new Date(), new Date());
        assertNotEquals(tenantUser1, tenantUser2);
    }

    @Test
    public void testEqualsWithDifferentStatus() {
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date());
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "inactive", "token123", new Date(), new Date());
        assertNotEquals(tenantUser1, tenantUser2);
    }

    @Test
    public void testEqualsWithDifferentInvitationToken() {
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date());
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token456", new Date(), new Date());
        assertNotEquals(tenantUser1, tenantUser2);
    }

    @Test
    public void testEqualsWithDifferentUpdatedAt() {
        Date now = new Date();
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", now, new Date());
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(now.getTime() + 1000), new Date());
        assertNotEquals(tenantUser1, tenantUser2);
    }

    @Test
    public void testEqualsWithDifferentCreatedAt() {
        Date now = new Date();
        TenantUser tenantUser1 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), now);
        TenantUser tenantUser2 = new TenantUser("1", List.of("ROLE_USER"), "tenant1", "active", "token123", new Date(), new Date(now.getTime() + 1000));
        assertNotEquals(tenantUser1, tenantUser2);
    }

}