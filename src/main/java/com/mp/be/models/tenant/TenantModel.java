package com.mp.be.models.tenant;

import com.mp.be.models.settings.SettingsModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
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