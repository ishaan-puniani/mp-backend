package com.mp.be.api.auth;

import com.mp.be.database.entities.Profile;
import com.mp.be.database.entities.User;
import com.mp.be.database.repositories.ProfileRepository;
import com.mp.be.models.auth.AuthenticationRequest;
import com.mp.be.models.auth.PasswordModel;
import com.mp.be.models.auth.ProfileDataModel;
import com.mp.be.models.auth.ResetPasswordModel;
import com.mp.be.models.user.UserModel;
import com.mp.be.services.auth.AuthenticationService;
import com.mp.be.services.user.UserService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ProfileRepository userprofileRepository;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setEmail("testUser@example.com");
        authRequest.setPassword("testPass");

        // Test success scenario
        when(authenticationService.signIn(any(AuthenticationRequest.class))).thenReturn("jwtToken");
        ResponseEntity<?> response = authenticationController.authenticate(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", response.getBody());

        // Test failure scenario
        when(authenticationService.signIn(any(AuthenticationRequest.class))).thenThrow(new BadCredentialsException("Invalid credentials"));
        response = authenticationController.authenticate(authRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sorry, we don't recognize your credentials", response.getBody());
    }

    @Test
    void testMe() {
        User currentUser = new User();
        currentUser.setId("1");
        when(request.getAttribute("currentUser")).thenReturn(currentUser);
        UserModel userModel = new UserModel();

        // Test success scenario
        when(userService.find(any(ServiceOptions.class), eq("1"))).thenReturn(userModel);
        ResponseEntity<UserModel> response = authenticationController.me(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userModel, response.getBody());

        // Test failure scenario
        when(userService.find(any(ServiceOptions.class), eq("1"))).thenThrow(new RuntimeException("User not found"));
        try {
            response = authenticationController.me(request);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    void testSignUp() {
        User user = new User();

        // Test success scenario
        when(authenticationService.registerUser(any(User.class))).thenReturn("jwtToken");
        ResponseEntity<String> response = authenticationController.signUp(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", response.getBody());

        // Test failure scenario
        when(authenticationService.registerUser(any(User.class))).thenThrow(new BadCredentialsException("User is already registered"));
        response = authenticationController.signUp(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User is already registered", response.getBody());
    }

    @Test
    void testChangePassword() {
        PasswordModel passwordModel = new PasswordModel();
        User currentUser = new User();
        when(request.getAttribute("currentUser")).thenReturn(currentUser);

        // Test success scenario
        when(authenticationService.changePassword(any(User.class), any(PasswordModel.class)))
                .thenReturn("Password changed successfully");
        ResponseEntity<String> response = authenticationController.changePassword(request, passwordModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());

        // Test failure scenario
        when(authenticationService.changePassword(any(User.class), any(PasswordModel.class)))
                .thenThrow(new RuntimeException("Invalid password"));
        try {
            response = authenticationController.changePassword(request, passwordModel);
        } catch (RuntimeException e) {
            assertEquals("Invalid password", e.getMessage());
        }
    }

    @Test
    void testSendPasswordResetEmail() {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", "testUser@example.com");

        // Test success scenario
        try {
            doNothing().when(authenticationService).sendPasswordResetEmail(any(String.class));
            ResponseEntity<String> response = authenticationController.sendPasswordResetEmail(payload);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Password reset email sent successfully.", response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Test failure scenario
        try {
            doThrow(new RuntimeException("Invalid email")).when(authenticationService).sendPasswordResetEmail(any(String.class));
            ResponseEntity<String> response = authenticationController.sendPasswordResetEmail(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Failed to send password reset email.", response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testResetPassword() {
        ResetPasswordModel resetPasswordModel = new ResetPasswordModel();

        // Test success scenario
        when(authenticationService.resetPassword(any(ResetPasswordModel.class)))
                .thenReturn("Password reset successfully");
        ResponseEntity<String> response = authenticationController.resetPassword(resetPasswordModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successfully", response.getBody());

        // Test failure scenario
        when(authenticationService.resetPassword(any(ResetPasswordModel.class)))
                .thenThrow(new RuntimeException("Invalid token"));
        try {
            response = authenticationController.resetPassword(resetPasswordModel);
        } catch (RuntimeException e) {
            assertEquals("Invalid token", e.getMessage());
        }
    }

    @Test
    void testVerifyEmail() {
        Map<String, String> payload = new HashMap<>();
        payload.put("token", "sampleToken");
        ServiceOptions serviceOptions = new ServiceOptions(request);

        // Test success scenario
        try {
            doNothing().when(authenticationService).verifyEmail(eq("sampleToken"), any(ServiceOptions.class));
            ResponseEntity<String> response = authenticationController.verifyEmail(payload, request);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Email verified successfully.", response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Test failure scenario
        try {
            doThrow(new BadCredentialsException("Verification failed")).when(authenticationService).verifyEmail(eq("sampleToken"), any(ServiceOptions.class));
            ResponseEntity<String> response = authenticationController.verifyEmail(payload, request);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Verification failed", response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendVerificationEmail() {
        // Test success scenario
        doNothing().when(authenticationService).sendEmailAddressVerificationEmail(any(ServiceOptions.class));
        ResponseEntity<String> response = authenticationController.sendVerificationEmail(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verification email sent successfully.", response.getBody());

        // Test failure scenario
        doThrow(new RuntimeException("Service error")).when(authenticationService).sendEmailAddressVerificationEmail(any(ServiceOptions.class));
        response = authenticationController.sendVerificationEmail(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Service error", response.getBody());
    }

    @Test
    void testUpdateProfile() {
        ProfileDataModel profileDataModel = new ProfileDataModel();
        Profile profile = new Profile();
        profile.setId("1");
        profileDataModel.data = profile;
        User currentUser = new User();
        currentUser.setEmail("test@example.com");
        when(request.getAttribute("currentUser")).thenReturn(currentUser);

        // Mock the repository to return a valid profile
        when(userprofileRepository.findById("1")).thenReturn(Optional.of(profile));

        // Test success scenario
        when(authenticationService.updateProfile(any(String.class), any(Profile.class)))
                .thenReturn(profile);
        Profile response = authenticationController.updateProfile(request, profileDataModel);
        assertEquals(profile, response);

        // Test failure scenario
        when(authenticationService.updateProfile(any(String.class), any(Profile.class)))
                .thenThrow(new RuntimeException("Invalid data"));
        try {
            response = authenticationController.updateProfile(request, profileDataModel);
        } catch (RuntimeException e) {
            assertEquals("Invalid data", e.getMessage());
        }
    }

} 