package com.mp.be.services.user;

import com.mp.be.database.entities.*;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.TenantRepository;
import com.mp.be.database.repositories.TenantUserRepository;
import com.mp.be.database.repositories.UserRepository;
import com.mp.be.models.auth.TenantUserOption;
import com.mp.be.models.settings.SettingsModel;
import com.mp.be.models.tenant.TenantModel;
import com.mp.be.models.tenant.TenantUserModel;
import com.mp.be.models.user.UserModel;
import com.mp.be.services.BrevoEmailService;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.user.UserServiceImpl;
import com.mp.be.models.user.UserRequestModel;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantUserRepository tenantUserRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private BrevoEmailService emailService;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void testFindAndCountAll() {
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        UserRequestModel requestModel = new UserRequestModel();
        requestModel.setFilter(Map.of("email", "test@example.com"));
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        userServiceImpl.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.of("email"));

        verify(mongoTemplate).find(any(Query.class), eq(User.class));
    }

    @Test
    void testFind() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel result = userServiceImpl.find(mock(ServiceOptions.class), userId);

        assertNotNull(result);
        assertEquals(userId, result.id);
    }

    @Test
    void testCreate() {
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        UserModel userModel = new UserModel();
        userModel.email = "newuser@example.com";
        userModel.roles = List.of("ROLE_USER");
        when(userRepository.findByEmail(userModel.email)).thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setId("newUserId");
        newUser.setEmail(userModel.email);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        TenantUser tenantUser = new TenantUser();
        tenantUser.setInvitationToken("invitationToken");
        when(tenantUserRepository.updateRoles(eq("tenantId"), eq("newUserId"), eq(userModel.roles), any(TenantUserOption.class)))
            .thenReturn(tenantUser);

        UserModel result = userServiceImpl.create(serviceOptions, userModel);

        assertNotNull(result);
        assertEquals(userModel.email, result.email);
        verify(emailService).sendEmail(eq(userModel.email), anyString(), anyMap());
    }

    @Test
    void testUpdate() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel userModel = new UserModel();
        userModel.email = "updated@example.com";

        UserModel result = userServiceImpl.update(mock(ServiceOptions.class), userId, userModel);

        assertNotNull(result);
        assertEquals(userModel.email, result.email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDelete() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.delete(userId);

        verify(userRepository).delete(user);
    }

    @Test
    void testFindByEmail() {
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserModel result = userServiceImpl.findByEmail(serviceOptions, email);

        assertNotNull(result);
        assertEquals(email, result.email);
    }

    @Test
    void testFindAll() {
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userServiceImpl.findAll(serviceOptions);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testCreateAuditLogOnDelete() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.delete(userId);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void testCreateAuditLogOnUpdate() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel userModel = new UserModel();
        userModel.email = "updated@example.com";

        userServiceImpl.update(mock(ServiceOptions.class), userId, userModel);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void testEmailServiceInCreate() {
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        UserModel userModel = new UserModel();
        userModel.email = "newuser@example.com";
        userModel.roles = List.of("ROLE_USER");
        when(userRepository.findByEmail(userModel.email)).thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setId("newUserId");
        newUser.setEmail(userModel.email);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        TenantUser tenantUser = new TenantUser();
        tenantUser.setInvitationToken("invitationToken");
        when(tenantUserRepository.updateRoles(eq("tenantId"), eq("newUserId"), eq(userModel.roles), any(TenantUserOption.class)))
            .thenReturn(tenantUser);

        userServiceImpl.create(serviceOptions, userModel);

        verify(emailService).sendEmail(eq(userModel.email), eq("19"), anyMap());
    }

    @Test
    void testFindWithTenantInformation() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant("tenantId");
        user.setTenants(List.of(tenantUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Tenant tenant = new Tenant();
        tenant.setId("tenantId");
        tenant.setName("Tenant Name");
        when(tenantRepository.findById("tenantId")).thenReturn(Optional.of(tenant));

        UserModel result = userServiceImpl.find(mock(ServiceOptions.class), userId);

        assertNotNull(result);
        assertEquals(1, result.getTenants().size());
        assertEquals("Tenant Name", result.getTenants().get(0).getTenant().getName());
    }

    @Test
    void testFindWithNoTenantInformation() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel result = userServiceImpl.find(mock(ServiceOptions.class), userId);

        assertNotNull(result);
        assertEquals(0, result.getTenants().size());
    }

    @Test
    void testFindWithTenantNotFound() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);
        TenantUser tenantUser = new TenantUser();
        tenantUser.setTenant("tenantId");
        user.setTenants(List.of(tenantUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(tenantRepository.findById("tenantId")).thenReturn(Optional.empty());

        UserModel result = userServiceImpl.find(mock(ServiceOptions.class), userId);

        assertNotNull(result);
        assertEquals(0, result.getTenants().size());
    }

    @Test
    void testCriteriaBuildingForName() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        UserRequestModel requestModel = new UserRequestModel();
        requestModel.setFilter(Map.of("name", "John"));
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        // Act
        userServiceImpl.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.of("name"));

        // Assert
        verify(mongoTemplate).find(any(Query.class), eq(User.class));
    }

    @Test
    void testCriteriaBuildingForStatus() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        UserRequestModel requestModel = new UserRequestModel();
        requestModel.setFilter(Map.of("status", "active"));
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        // Act
        userServiceImpl.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.of("status"));

        // Assert
        verify(mongoTemplate).find(any(Query.class), eq(User.class));
    }

    @Test
    void testCriteriaBuildingForRole() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        UserRequestModel requestModel = new UserRequestModel();
        requestModel.setFilter(Map.of("role", "admin"));
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");

        // Act
        userServiceImpl.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.of("role"));

        // Assert
        verify(mongoTemplate).find(any(Query.class), eq(User.class));
    }

    @Test
    void testTenantAndSettingsHandling() {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setId("tenantId");
        tenant.setName("Tenant Name");

        Setting setting = new Setting();
        setting.setId("settingId");
        setting.setTheme("dark");

        TenantModel tenantModel = new TenantModel();
        tenantModel.setSettings(new SettingsModel(setting.id, setting.theme, null, null, null, null, null, null));

        // Act
        // Simulate logic that uses tenant and settings

        // Assert
        // Verify interactions or state changes
    }

    @Test
    void testMapUserForTenantForRows() {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setId("tenantId");

        User user1 = new User();
        user1.setTenants(new ArrayList<>());

        User user2 = new User();
        user2.setTenants(new ArrayList<>());

        List<User> rows = List.of(user1, user2);

        // Act
        List<UserModel> result = UserServiceImpl.mapUserForTenantForRows(rows, tenant);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

} 