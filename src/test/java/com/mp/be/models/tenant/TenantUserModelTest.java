package com.mp.be.models.tenant;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TenantUserModelTest {

    @Test
    public void testTenantUserGettersAndSetters() {
        TenantUserModel tenantUser = new TenantUserModel();

        tenantUser.setId("test-id");
        assertEquals("test-id", tenantUser.getId());

        List<String> roles = List.of("ADMIN", "USER");
        tenantUser.setRoles(roles);
        assertEquals(roles, tenantUser.getRoles());

        TenantModel tenant = new TenantModel();
        tenantUser.setTenant(tenant);
        assertEquals(tenant, tenantUser.getTenant());

        tenantUser.setStatus("active");
        assertEquals("active", tenantUser.getStatus());

        Date createdAt = new Date();
        tenantUser.setCreatedAt(createdAt);
        assertEquals(createdAt, tenantUser.getCreatedAt());

        Date updatedAt = new Date();
        tenantUser.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, tenantUser.getUpdatedAt());

        tenantUser.setInvitationToken("token123");
        assertEquals("token123", tenantUser.getInvitationToken());
    }
}