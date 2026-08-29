/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.user;

import com.mp.be.models.tenant.TenantUserModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@Schema(name = "User_Model", description = "System User Account Model")
public class UserModel {
    public String id;
    public String email;
    public String password;
    public Boolean emailVerified;
    public List<TenantUserModel> tenants;
    public List<String> roles;
    public String status;
}
