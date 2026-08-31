package com.mp.be.models.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Auth_SignUpRequest", description = "Registration Credentials Request Model")
public class SignUpRequest {
    @Schema(description = "User email address", example = "user@example.com")
    public String email;

    @Schema(description = "User password", example = "password123")
    public String password;
}
