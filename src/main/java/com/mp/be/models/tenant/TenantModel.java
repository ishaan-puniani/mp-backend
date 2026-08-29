package com.mp.be.models.tenant;

import com.mp.be.models.settings.SettingsModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@Schema(name = "Tenant_Model", description = "Tenant Organization Model")
public class TenantModel {
    public String id;
    public String name;
    public String url;
    public String plan;
    public String planStatus;
    public String createdBy;
    public String updatedBy;
    public Date createdAt;
    public Date updatedAt;
    public SettingsModel settings;
}