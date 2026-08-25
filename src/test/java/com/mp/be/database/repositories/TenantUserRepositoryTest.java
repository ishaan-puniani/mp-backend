package com.mp.be.database.repositories;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.TenantUser;
import com.mp.be.database.entities.User;
import com.mp.be.models.auth.TenantUserOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TenantUserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private TenantUserRepository tenantUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setId("tenantId");
        User user = new User();
        user.setId("userId");
        List<String> roles = List.of("role1", "role2");

        when(mongoTemplate.findById(user.getId(), User.class)).thenReturn(user);

        // Act
        tenantUserRepository.create(tenant, user, roles);

        // Assert
        verify(mongoTemplate, times(1)).save(user);
        verify(mongoTemplate, times(1)).save(any(TenantUser.class));
        assertNotNull(user.getTenants());
        assertEquals(1, user.getTenants().size());
        assertEquals(roles, user.getTenants().get(0).getRoles());
        assertEquals("active", user.getTenants().get(0).getStatus());
    }

    @Test
    void testUpdateRoles() {
        // Arrange
        String tenantId = "tenantId";
        String userId = "userId";
        List<String> roles = List.of("role1", "role2");
        TenantUserOption options = new TenantUserOption();
        User user = new User();
        user.setId(userId);
        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant(tenantId);
        user.setTenants(List.of(tenantUser));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(new Tenant()));

        // Act
        tenantUserRepository.updateRoles(tenantId, userId, roles, options);

        // Assert
        verify(mongoTemplate).save(user);
        assertEquals(roles, tenantUser.getRoles());
        assertEquals("active", tenantUser.getStatus());
    }

    @Test
    void testCreateWithExistingUser() {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setId("tenantId");
        User user = new User();
        user.setId("userId");
        List<String> roles = List.of("role1", "role2");

        when(mongoTemplate.findById(user.getId(), User.class)).thenReturn(user);

        // Act
        tenantUserRepository.create(tenant, user, roles);

        // Assert
        verify(mongoTemplate, times(1)).save(user);
        verify(mongoTemplate, times(1)).save(any(TenantUser.class));
    }

    @Test
    void testCreateWithNonExistingUser() {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setId("tenantId");
        User user = new User();
        user.setId("userId");
        List<String> roles = List.of("role1", "role2");

        when(mongoTemplate.findById(user.getId(), User.class)).thenReturn(null);

        // Act
        tenantUserRepository.create(tenant, user, roles);

        // Assert
        verify(mongoTemplate, never()).save(any(User.class));
    }

    @Test
    void testUpdateRolesWithNoTenants() {
        // Arrange
        String tenantId = "tenantId";
        String userId = "userId";
        List<String> roles = List.of("role1", "role2");
        TenantUserOption options = new TenantUserOption();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(new Tenant()));

        // Act
        tenantUserRepository.updateRoles(tenantId, userId, roles, options);

        // Assert
        verify(mongoTemplate, times(2)).save(user);
    }

    @Test
    void testUpdateRolesWithExistingTenant() {
        // Arrange
        String tenantId = "tenantId";
        String userId = "userId";
        List<String> roles = List.of("role1", "role2");
        TenantUserOption options = new TenantUserOption();
        User user = new User();
        user.setId(userId);
        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant(tenantId);
        user.setTenants(List.of(tenantUser));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(new Tenant()));

        // Act
        tenantUserRepository.updateRoles(tenantId, userId, roles, options);

        // Assert
        verify(mongoTemplate).save(user);
    }
} 