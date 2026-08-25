/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.settings;

import com.mp.be.database.entities.*;
import com.mp.be.database.entities.Setting;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.FileRepository;
import com.mp.be.database.repositories.SettingsRepository;
import com.mp.be.database.repositories.TenantRepository;
import com.mp.be.models.settings.SettingsRequestModel;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Autowired
    private SettingsRepository repository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Setting find(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Setting findOrCreate(Tenant tenant) {
        Setting tenantSetting =  repository.findByTenantId(tenant.id);
        if(tenantSetting == null){
            tenantSetting = new Setting();
            tenantSetting.setTenant(tenant.id);
            tenantSetting.setCreatedBy(tenant.id);
            tenantSetting.setUpdatedBy(tenant.id);
            tenantSetting.theme = "default";
            tenantSetting = repository.save(tenantSetting);
        }
        tenant.setSettings(tenantSetting);
        tenantRepository.save(tenant);
        return tenantSetting;
    }


    @Override
    public Setting create(Setting data) {
        Setting record = repository.save(data);
        createAuditLog("create", data, record);
        return record;
    }

    @Override
    public void delete(String id) {
        Setting record = repository.findById(id).orElse(null);
        repository.delete(record);
        createAuditLog("delete", null, record);
    }

    @Override
    public Setting update(String id, Setting data) {
        Setting record = repository.findById(id).orElse(null);
        if (record != null) {
            if (data.getLogos() != null) {
                saveFileModels(data.getLogos());
                record.setLogos(new ArrayList<>(data.getLogos()));
            } else {
                record.setLogos(new ArrayList<>());
            }

            if (data.getBackgroundImages() != null) {
                saveFileModels(data.getBackgroundImages());
                record.setBackgroundImages(new ArrayList<>(data.getBackgroundImages()));
            } else {
                record.setBackgroundImages(new ArrayList<>());
            }
            // Copy properties from 'data' to 'record', ignoring null values
            BeanUtils.copyProperties(data, record, "id","logos","backgroundImages");
            // Save the updated record
            repository.save(record);
            createAuditLog("update", data, record);
        }
        return record;
    }
    
    private void createAuditLog(String action, Setting data, Setting record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("Setting");
        auditLog.setAction(action);
        auditLog.setEntityId(record.id);

        if(data != null) {
            // mode to json
            Gson gson = new Gson();
            String jsonStr = gson.toJson(data);
            Map<String,Object> map = new HashMap<String,Object>();
            map = (Map<String,Object>) gson.fromJson(jsonStr, map.getClass());
            auditLog.setValues(map);
        }

        auditLogRepository.save(auditLog);
    }

    private void saveFileModels(List<File> fileModels) {
        if (fileModels != null) {
            for (File fileModel : fileModels) {
                if (fileRepository.findById(fileModel.id).isEmpty()) {
                    fileModel.setId(String.valueOf(new ObjectId()));
                    fileRepository.save(fileModel);
                }
            }
        }
    }
}
