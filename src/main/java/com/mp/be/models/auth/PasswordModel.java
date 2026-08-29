/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Auth_PasswordModel", description = "Password Change Request Model")
public class PasswordModel {

    String oldPassword;
    String newPassword;
}