package com.mp.be.models.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Auth_ResetPasswordModel", description = "Password Reset Token Confirmation Model")
public class ResetPasswordModel {


    String password;
    String token;
}
