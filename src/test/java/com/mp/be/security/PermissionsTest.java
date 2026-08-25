package com.mp.be.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermissionsTest {

    @Test
    public void testPermissions() {
        assertEquals("admin:read", Permissions.ADMIN_READ.getPermission());
        assertEquals("admin:update", Permissions.ADMIN_UPDATE.getPermission());
        assertEquals("admin:create", Permissions.ADMIN_CREATE.getPermission());
        assertEquals("admin:delete", Permissions.ADMIN_DELETE.getPermission());
        assertEquals("management:read", Permissions.MANAGER_READ.getPermission());
        assertEquals("management:update", Permissions.MANAGER_UPDATE.getPermission());
        assertEquals("management:create", Permissions.MANAGER_CREATE.getPermission());
        assertEquals("management:delete", Permissions.MANAGER_DELETE.getPermission());
    }
} 