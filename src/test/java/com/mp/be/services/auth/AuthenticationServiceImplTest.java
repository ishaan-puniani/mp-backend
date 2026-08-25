package com.mp.be.services.auth;

import com.mp.be.database.entities.Profile;
import com.mp.be.database.entities.User;
import com.mp.be.database.repositories.ProfileRepository;
import com.mp.be.database.repositories.UserRepository;
import com.mp.be.models.auth.AuthenticationRequest;
import com.mp.be.models.auth.PasswordModel;
import com.mp.be.services.BrevoEmailService;
import com.mp.be.services.JwtService;
import com.mp.be.services.auth.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mp.be.database.entities.File;
import com.mp.be.database.repositories.FileRepository;
import com.mp.be.models.auth.ResetPasswordModel;
import com.mp.be.services.ServiceOptions;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.function.Function;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileRepository userprofileRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private BrevoEmailService emailService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignInSuccess() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        // Act
        String token = authenticationService.signIn(request);

        // Assert
        assertEquals("jwtToken", token);
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), user.getPassword());
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void testSignInFailure() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.signIn(request));
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), user.getPassword());
    }

    @Test
    void testRegisterUserSuccess() {
        // Arrange
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("newPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        // Act
        String token = authenticationService.registerUser(user);

        // Assert
        assertEquals("jwtToken", token);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");
        existingUser.setPassword("existingPassword");

        User newUser = new User();
        newUser.setEmail("existinguser@example.com");
        newUser.setPassword("newPassword");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.registerUser(newUser));
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePasswordSuccess() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedOldPassword");

        PasswordModel model = new PasswordModel();
        model.setOldPassword("oldPassword");
        model.setNewPassword("newPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(model.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(model.getNewPassword())).thenReturn("encodedNewPassword");

        // Act
        String message = authenticationService.changePassword(user, model);

        // Assert
        assertEquals("Password Changed Successfully", message);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePasswordFailure() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedOldPassword");

        PasswordModel model = new PasswordModel();
        model.setOldPassword("wrongOldPassword");
        model.setNewPassword("newPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(model.getOldPassword(), user.getPassword())).thenReturn(false);

        // Act
        String message = authenticationService.changePassword(user, model);

        // Assert
        assertEquals("Failed to change password. Please check your credentials.", message);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateProfile() {
        // Arrange
        String userId = "userId";
        Profile profile = new Profile();
        profile.setAvatars(List.of(new File()));

        Profile existingProfile = new Profile();
        existingProfile.setAvatars(new ArrayList<>());

        when(userprofileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));
        when(userprofileRepository.save(any(Profile.class))).thenReturn(existingProfile);

        // Act
        Profile updatedProfile = authenticationService.updateProfile(userId, profile);

        // Assert
        assertNotNull(updatedProfile);
        assertEquals(profile.getAvatars(), updatedProfile.getAvatars());
        verify(userprofileRepository, times(1)).findById(userId);
        verify(userprofileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testSendPasswordResetEmail() throws Exception {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        authenticationService.sendPasswordResetEmail(email);

        // Assert
        verify(emailService).sendEmail(eq(email), eq("22"), anyMap());
    }

    @Test
    void testResetPasswordSuccess() {
        // Arrange
        ResetPasswordModel model = new ResetPasswordModel();
        model.setToken("resetToken");
        model.setPassword("newPassword");

        User user = new User();
        user.setPasswordResetToken("resetToken");
        when(userRepository.findByPasswordResetToken(model.getToken())).thenReturn(Optional.of(user));

        // Act
        String message = authenticationService.resetPassword(model);

        // Assert
        assertEquals("Password Reset Successfully", message);
        verify(userRepository).save(user);
    }

    @Test
    void testVerifyEmail() {
        // Arrange
        String token = "verificationToken";
        User user = new User();
        user.setId("userId");
        user.setEmailVerificationToken(token);
        when(userRepository.findByEmailVerificationToken(token)).thenReturn(Optional.of(user));

        ServiceOptions options = mock(ServiceOptions.class);
        when(options.getCurrentUser()).thenReturn(user);

        // Act
        authenticationService.verifyEmail(token, options);

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void testSendEmailAddressVerificationEmail() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        ServiceOptions options = mock(ServiceOptions.class);
        when(options.getCurrentUser()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        authenticationService.sendEmailAddressVerificationEmail(options);

        // Assert
        verify(emailService).sendEmail(eq(user.getEmail()), eq("17"), anyMap());
    }

    @Test
    void testExtractClaim() {
        String token = "testToken";
        Function<Claims, String> claimsResolver = Claims::getSubject;
        when(jwtService.extractClaim(eq(token), any())).thenReturn("testSubject");

        String claim = jwtService.extractClaim(token, claimsResolver);

        assertNotNull(claim);
        assertEquals("testSubject", claim);
        verify(jwtService, times(1)).extractClaim(eq(token), any());
    }

    @Test
    void testRegisterUserUpdateExistingUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");
        existingUser.setPassword(null); // No password set

        User newUser = new User();
        newUser.setEmail("existinguser@example.com");
        newUser.setPassword("newPassword");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        // Act
        String token = authenticationService.registerUser(newUser);

        // Assert
        assertEquals("jwtToken", token);
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testGetUsers() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = authenticationService.getUsers(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = List.of(new User(), new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        Iterable<User> result = authenticationService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(3, ((List<User>) result).size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUsers() {
        // Arrange
        String userId = "userId";
        doNothing().when(userRepository).deleteById(userId);

        // Act
        authenticationService.deletUsers(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testChangePasswordFailureWrongUser() {
        // Arrange
        User currentUser = new User();
        currentUser.setId("currentUserId");
        currentUser.setEmail("current@example.com");

        User user = new User();
        user.setId("userId");
        user.setEmail("user@example.com");

        ServiceOptions options = mock(ServiceOptions.class);
        when(options.getCurrentUser()).thenReturn(currentUser);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.verifyEmail("token", options));
    }

    @Test
    void testSendEmailAddressVerificationEmailFailure() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        ServiceOptions options = mock(ServiceOptions.class);
        when(options.getCurrentUser()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doThrow(new RuntimeException()).when(emailService).sendEmail(anyString(), anyString(), anyMap());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.sendEmailAddressVerificationEmail(options));
    }

    @Test
    void testSaveProfileRecord() {
        // Arrange
        String userId = "userId";
        Profile existingProfile = new Profile();
        Profile newProfileData = new Profile();
        newProfileData.setAvatars(List.of(new File())); // Ensure some data change

        when(userprofileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));
        when(userprofileRepository.save(any(Profile.class))).thenReturn(existingProfile);

        // Act
        authenticationService.updateProfile(userId, newProfileData);

        // Assert
        verify(userprofileRepository, times(1)).save(existingProfile);
    }

    @Test
    void testSaveFileModels() throws Exception {
        // Arrange
        File fileModel = new File();
        fileModel.setId("fileId");
        when(fileRepository.findById(fileModel.getId())).thenReturn(null);

        // Use reflection to access the private method
        Method method = AuthenticationServiceImpl.class.getDeclaredMethod("saveFileModels", List.class);
        method.setAccessible(true);

        // Act
        method.invoke(authenticationService, List.of(fileModel));

        // Assert
        verify(fileRepository, times(1)).save(fileModel);
    }
}