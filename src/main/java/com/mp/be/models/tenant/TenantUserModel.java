package com.mp.be.models.tenant;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@Schema(name = "Tenant_UserModel", description = "Tenant User Membership and Roles Model")
public class TenantUserModel {
    public String id;
    public List<String> roles;
    public TenantModel tenant;
    public String status;
    public Date updatedAt;
    public Date createdAt;
    private String invitationToken;
}