package com.mp.be.api.settings;

import com.mp.be.database.entities.Setting;
import com.mp.be.database.entities.Tenant;
import com.mp.be.models.settings.SettingsDataModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.settings.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "10. Settings", description = "Tenant and application level configuration parameters")
@RestController
@RequestMapping("/api/tenant/{tenantId}/settings")
public class SettingsController {

    @Autowired
    private SettingsService service;

    @Operation(summary = "Get or create settings for current tenant")
    @GetMapping("")
    public ResponseEntity<Setting> find(HttpServletRequest request) {
        Tenant currentTenant = (Tenant) request.getAttribute("currentTenant");
        Setting tenantSetting = service.findOrCreate(currentTenant);
        return ResponseEntity.ok(tenantSetting);
    }

    @Operation(summary = "Get setting by ID")
    @GetMapping("/{id}")
    public Setting find(@PathVariable String id) {
        return service.find(id);
    }

    @Operation(summary = "Update tenant configuration settings")
    @PutMapping("")
    public Setting update(HttpServletRequest request, @RequestBody SettingsDataModel body) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Setting data = body.settings;
        data.setTenant(serviceOptions.getCurrentTenantId());
        return service.update(data.id, data);
    }
}