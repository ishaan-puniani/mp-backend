/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Auth_TenantUserOption", description = "Tenant User Role Assignment Option")
public class TenantUserOption {
    public  boolean isAddRoles;
    public  boolean  isRemoveOnlyInformedRoles;
}
