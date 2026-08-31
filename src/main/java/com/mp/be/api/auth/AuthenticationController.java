package com.mp.be.api.auth;

import com.mp.be.database.entities.Profile;
import com.mp.be.database.entities.User;
import com.mp.be.models.auth.AuthenticationRequest;
import com.mp.be.models.auth.PasswordModel;
import com.mp.be.models.auth.ProfileDataModel;
import com.mp.be.models.auth.ResetPasswordModel;
import com.mp.be.models.user.UserModel;
import com.mp.be.models.user.UserMeModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.auth.AuthenticationService;
import com.mp.be.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "05. Authentication & Access", description = "User authentication, JWT login, registration, password recovery, and profile")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get current authenticated user profile")
    @GetMapping("/me")
    public ResponseEntity<UserMeModel> me(HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        User currentUser = (User) request.getAttribute("currentUser");
        UserMeModel userModel = userService.findMe(serviceOptions, currentUser.getId());
        return ResponseEntity.ok(userModel);
    }

    @Operation(summary = "Login with email and password to receive JWT bearer token")
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            String jwtToken = service.signIn(request);
            return ResponseEntity.ok(jwtToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Sorry, we don't recognize your credentials");
        }
    }

    @Operation(summary = "Register a new platform user")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        try {
            String jwtToken = service.registerUser(user);
            return ResponseEntity.ok(jwtToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User is already registered");
        }
    }

    @Operation(summary = "Change password for currently authenticated user")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(HttpServletRequest request, @RequestBody PasswordModel model) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        String message = service.changePassword(serviceOptions.getCurrentUser(), model);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Send password reset email to user")
    @PostMapping("/send-password-reset-email")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            service.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send password reset email.");
        }
    }

    @Operation(summary = "Reset password using reset token")
    @PutMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordModel model) {
        String message = service.resetPassword(model);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Verify user email address with token")
    @PutMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String token = payload.get("token");
        ServiceOptions serviceOptions = new ServiceOptions(request);
        try {
            service.verifyEmail(token, serviceOptions);
            return ResponseEntity.ok("Email verified successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Send email address verification email")
    @PostMapping("/send-email-address-verification-email")
    public ResponseEntity<String> sendVerificationEmail(HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        try {
            service.sendEmailAddressVerificationEmail(serviceOptions);
            return ResponseEntity.ok("Verification email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/profile")
    public Profile updateProfile(HttpServletRequest request, @RequestBody ProfileDataModel body) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Profile profile = body.data;
        profile.setEmail(serviceOptions.getCurrentUser().getEmail());
        profile.setTenantId(serviceOptions.getCurrentTenantId());
        return service.updateProfile(profile.id, profile);
    }
}