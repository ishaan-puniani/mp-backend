package com.mp.be.security;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RolesTest {

    @Test
    public void testUserRolePermissions() {
        assertEquals(Set.of(), Roles.USER.getPermissions());
    }

    @Test
    public void testAdminRolePermissions() {
        assertEquals(Set.of(
                Permissions.ADMIN_READ,
                Permissions.ADMIN_UPDATE,
                Permissions.ADMIN_DELETE,
                Permissions.ADMIN_CREATE,
                Permissions.MANAGER_READ,
                Permissions.MANAGER_UPDATE,
                Permissions.MANAGER_DELETE,
                Permissions.MANAGER_CREATE
        ), Roles.ADMIN.getPermissions());
    }

    @Test
    public void testManagerRolePermissions() {
        assertEquals(Set.of(
                Permissions.MANAGER_READ,
                Permissions.MANAGER_UPDATE,
                Permissions.MANAGER_DELETE,
                Permissions.MANAGER_CREATE
        ), Roles.MANAGER.getPermissions());
    }


} 