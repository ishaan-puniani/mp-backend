package com.mp.be.models.tenant;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class TenantUserModel {
    public String id;
    public List<String> roles;
    public TenantModel tenant;
    public String status;
    public Date updatedAt;
    public Date createdAt;
    private String invitationToken;
}