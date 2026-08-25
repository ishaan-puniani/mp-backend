/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.settings;

import com.mp.be.database.entities.Setting;
import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.User;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.settings.SettingsService;
import com.mp.be.models.settings.SettingsDataModel;
import com.mp.be.models.settings.SettingsModel;
import com.mp.be.services.ServiceOptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tenant/{tenantId}/settings")
public class SettingsController {
    @Autowired
    private SettingsService service;

    @GetMapping("")
    public ResponseEntity<Setting> find(HttpServletRequest request
                                     ) {

        Tenant currentTenant = (Tenant) request.getAttribute("currentTenant");
        Setting tenantSetting = service.findOrCreate(currentTenant);
        return ResponseEntity.ok(tenantSetting);
    }

    
    @GetMapping("/{id}")
    public Setting find(@PathVariable String id ){
        return service.find(id);
    }

    

    @PutMapping("")
    public Setting update(HttpServletRequest request, @RequestBody SettingsDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);

        Setting data = body.settings;
        data.setTenant(serviceOptions.getCurrentTenantId());

        return service.update(data.id, data);
    }
//
//    @DeleteMapping("")
//    public void delete(@RequestParam(name = "ids[]") List<String> ids){
//        for (String id : ids) {
//            service.delete(id);
//        }
//    }
}