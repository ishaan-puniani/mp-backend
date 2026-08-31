package com.mp.be.models.user;

import com.mp.be.models.tenant.TenantUserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User_Me_Model", description = "Current Authenticated User Profile Response Model for /api/auth/me")
public class UserMeModel {
    public String id;
    public String email;
    public Boolean emailVerified;
    public List<TenantUserModel> tenants;
}
