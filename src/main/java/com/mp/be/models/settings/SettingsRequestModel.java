/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(name = "Settings_RequestModel", description = "Tenant Settings Query and Filter Request Model")
public class SettingsRequestModel {
    private Map<String, Object> filter;

    public SettingsRequestModel(){}

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }
}
