package com.mp.be.services.tenant;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.TenantUser;
import com.mp.be.database.entities.User;
import com.mp.be.database.repositories.TenantRepository;
import com.mp.be.database.repositories.TenantUserRepository;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.models.tenant.TenantRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.tenant.TenantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpServletRequest;

class TenantServiceImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private TenantUserRepository tenantUserRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private TenantServiceImpl tenantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTenant() {
        // Arrange
        User currentUser = new User();
        currentUser.setId("userId");

        Tenant tenant = new Tenant();
        tenant.setName("New Tenant");

        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Tenant createdTenant = tenantService.create(tenant, currentUser);

        // Assert
        assertNotNull(createdTenant);
        assertEquals("New Tenant", createdTenant.getName());
        assertEquals("FREE", createdTenant.getPlan());
        assertEquals("ACTIVE", createdTenant.getPlanStatus());
        assertEquals("userId", createdTenant.getPlanUserId());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
        verify(tenantUserRepository, times(1)).create(any(Tenant.class), eq(currentUser), eq(List.of("admin")));
    }

    @Test
    void testCreateTenantWithExplicitPlan() {
        // Arrange
        User currentUser = new User();
        currentUser.setId("userId");

        Tenant tenant = new Tenant();
        tenant.setName("Enterprise Tenant");
        tenant.setPlan("ENTERPRISE");
        tenant.setPlanStatus("TRIALING");

        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Tenant createdTenant = tenantService.create(tenant, currentUser);

        // Assert
        assertNotNull(createdTenant);
        assertEquals("ENTERPRISE", createdTenant.getPlan());
        assertEquals("TRIALING", createdTenant.getPlanStatus());
    }

    @Test
    void testFindAndCountAll() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        User currentUser = new User();

        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant("tenantId");
        
        currentUser.setTenants(List.of(tenantUser));
        when(serviceOptions.getCurrentUser()).thenReturn(currentUser);

        TenantRequestModel requestModel = new TenantRequestModel();
        Page<Tenant> expectedPage = new PageImpl<>(List.of(new Tenant()));

        when(mongoTemplate.find(any(), eq(Tenant.class))).thenReturn(expectedPage.getContent());
        when(mongoTemplate.count(any(), eq(Tenant.class))).thenReturn(1L);

        // Act
        Page<Tenant> resultPage = tenantService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        verify(mongoTemplate, times(1)).find(any(), eq(Tenant.class));
        verify(mongoTemplate, times(1)).count(any(), eq(Tenant.class));
    }

    @Test
    void testAcceptInvitation() {
        // Arrange
        String invitationToken = "validToken";
        User currentUser = new User();
        currentUser.setId("userId");

        TenantUser invitationTenantUser = new TenantUser();
        invitationTenantUser.setTenant("tenantId");
        invitationTenantUser.setInvitationToken(invitationToken);

        User userWithInvitation = new User();
        userWithInvitation.setTenants(List.of(invitationTenantUser));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("currentUser")).thenReturn(currentUser);

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(userWithInvitation);

        when(tenantRepository.findById("tenantId")).thenReturn(Optional.of(new Tenant()));

        // Act
        Tenant tenant = tenantService.acceptInvitation(invitationToken, currentUser, new ServiceOptions(request));

        // Assert
        assertNotNull(tenant);
        verify(mongoTemplate, times(1)).findOne(any(Query.class), eq(User.class));
        verify(tenantRepository, times(1)).findById("tenantId");
    }

    @Test
    void testAcceptInvitationWithNullToken() {
        // Arrange
        User currentUser = new User();
        currentUser.setId("userId");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tenantService.acceptInvitation(null, currentUser, new ServiceOptions(mock(HttpServletRequest.class)));
        });
    }

    @Test
    void testDestroyTenant() {
        // Arrange
        String tenantId = "tenantId";
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant(tenantId);
        user.setTenants(new ArrayList<>(List.of(tenantUser)));

        when(mongoTemplate.findById(userId, User.class)).thenReturn(user);

        // Act
        tenantService.destroy(tenantId, userId, new ServiceOptions(mock(HttpServletRequest.class)));

        // Assert
        assertTrue(user.getTenants().isEmpty());
        verify(mongoTemplate, times(1)).save(user);
    }

    @Test
    void testFindAndCountAllWithEmptyTenants() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        User currentUser = new User();
        currentUser.setTenants(List.of());
        when(serviceOptions.getCurrentUser()).thenReturn(currentUser);

        TenantRequestModel requestModel = new TenantRequestModel();

        // Act
        Page<Tenant> resultPage = tenantService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertNotNull(resultPage);
        assertEquals(0, resultPage.getTotalElements());
    }

    @Test
    void testMergeRoles() {
        // Arrange
        TenantUser existingTenantUser = new TenantUser();
        existingTenantUser.setRoles(List.of("admin", "user"));

        TenantUser invitationTenantUser = new TenantUser();
        invitationTenantUser.setRoles(List.of("user", "manager"));

        // Act
        tenantService.mergeRoles(existingTenantUser, invitationTenantUser);

        // Assert
        assertEquals(3, existingTenantUser.getRoles().size());
        assertTrue(existingTenantUser.getRoles().containsAll(List.of("admin", "user", "manager")));
    }

    @Test
    void testFindAndCountAllWithDifferentLimits() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        User currentUser = new User();
        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant("tenantId");
        currentUser.setTenants(List.of(tenantUser));
        when(serviceOptions.getCurrentUser()).thenReturn(currentUser);

        TenantRequestModel requestModel = new TenantRequestModel();
        Page<Tenant> expectedPage = new PageImpl<>(List.of(new Tenant()));

        when(mongoTemplate.find(any(), eq(Tenant.class))).thenReturn(expectedPage.getContent());
        when(mongoTemplate.count(any(), eq(Tenant.class))).thenReturn(1L);

        // Act
        Page<Tenant> resultPage = tenantService.findAndCountAll(serviceOptions, requestModel, Optional.of(5), Optional.of(0), Optional.of("name"));

        // Assert
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        verify(mongoTemplate, times(1)).find(any(), eq(Tenant.class));
        verify(mongoTemplate, times(1)).count(any(), eq(Tenant.class));
    }

    @Test
    void testUpdateOrAddTenantUser() {
        // Arrange
        User currentUser = new User();
        TenantUser tenantUserToAdd = new TenantUser();
        tenantUserToAdd.setTenant("tenantId");

        // Act
        tenantService.updateOrAddTenantUser(currentUser, tenantUserToAdd);

        // Assert
        assertEquals(1, currentUser.getTenants().size());
        assertEquals("tenantId", currentUser.getTenants().get(0).getTenant());
    }

    @Test
    void testAcceptInvitationWithValidToken() {
        // Arrange
        String invitationToken = "validToken";
        User currentUser = new User();
        currentUser.setId("userId");

        TenantUser invitationTenantUser = new TenantUser();
        invitationTenantUser.setTenant("tenantId");
        invitationTenantUser.setInvitationToken(invitationToken);

        currentUser.setTenants(List.of(invitationTenantUser));

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(currentUser);
        when(tenantRepository.findById("tenantId")).thenReturn(Optional.of(new Tenant()));

        // Act
        Tenant tenant = tenantService.acceptInvitation(invitationToken, currentUser, new ServiceOptions(mock(HttpServletRequest.class)));

        // Assert
        assertNotNull(tenant);
        verify(mongoTemplate, times(1)).findOne(any(Query.class), eq(User.class));
        verify(tenantRepository, times(1)).findById("tenantId");
    }

    @Test
    void testAcceptInvitationWithNewTenantUser() {
        // Arrange
        String invitationToken = "validToken";
        User currentUser = new User();
        currentUser.setId("userId");

        TenantUser invitationTenantUser = new TenantUser();
        invitationTenantUser.setTenant("newTenantId");
        invitationTenantUser.setInvitationToken(invitationToken);
        invitationTenantUser.setRoles(List.of("admin"));

        currentUser.setTenants(new ArrayList<>(List.of(invitationTenantUser)));

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(currentUser);
        when(tenantRepository.findById("newTenantId")).thenReturn(Optional.of(new Tenant()));

        // Act
        Tenant tenant = tenantService.acceptInvitation(invitationToken, currentUser, new ServiceOptions(mock(HttpServletRequest.class)));

        // Assert
        assertNotNull(tenant);
        assertEquals(1, currentUser.getTenants().size());
        assertEquals("newTenantId", currentUser.getTenants().get(0).getTenant());
        verify(mongoTemplate, times(1)).findOne(any(Query.class), eq(User.class));
        verify(tenantRepository, times(1)).findById("newTenantId");
    }

} 