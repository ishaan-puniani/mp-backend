/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.settings;

import com.mp.be.database.entities.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "Settings_Model", description = "Tenant Branding and UI Settings Model")
public class SettingsModel {
    public String id;
    public String theme;
    private List<File> logos;
    private List<File> backgroundImages;
    private Date createdAt;
    private Date updatedAt;
    public String createdBy;
    public String updatedBy;
}
