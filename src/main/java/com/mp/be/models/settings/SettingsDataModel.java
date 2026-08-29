/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.settings;

import com.mp.be.database.entities.Setting;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Settings_DataModel", description = "Tenant Settings Single Data Wrapper")
public class SettingsDataModel {
    public Setting settings;
}
