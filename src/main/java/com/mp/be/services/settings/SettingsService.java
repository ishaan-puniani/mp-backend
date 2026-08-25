/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.settings;

import com.mp.be.database.entities.Setting;
import com.mp.be.database.entities.Tenant;
import com.mp.be.models.settings.SettingsRequestModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SettingsService {
 
    public Setting create(Setting data);

    public Setting find(String id);
    public Setting findOrCreate(Tenant tenant);
    public void delete(String id );

    public Setting update(String id , Setting data);
}
