package com.mp.be.services.process;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.entities.MachineMaster;
import com.mp.be.database.entities.MaterialMaster;
import com.mp.be.database.entities.process.*;
import com.mp.be.database.enumerator.ItemStatus;
import com.mp.be.database.enumerator.ProcessStatus;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.MachineMasterRepository;
import com.mp.be.database.repositories.MaterialMasterRepository;
import com.mp.be.database.repositories.ProcessConfigurationRepository;
import com.mp.be.models.process.ProcessConfigurationModel;
import com.mp.be.models.process.ProcessConfigurationRequestModel;
import com.mp.be.services.ServiceOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class ProcessConfigurationServiceImpl implements ProcessConfigurationService {

    @Autowired
    private ProcessConfigurationRepository repository;

    @Autowired
    private ProcessConfigurationValidator validator;

    @Autowired
    private MaterialMasterRepository materialMasterRepository;

    @Autowired
    private MachineMasterRepository machineMasterRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    public ProcessConfigurationServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Page<ProcessConfigurationModel> findAndCountAll(ServiceOptions serviceOptions,
                                                           ProcessConfigurationRequestModel requestModel,
                                                           Optional<Integer> limit,
                                                           Optional<Integer> offset,
                                                           Optional<String> orderBy) {
        Criteria criteria = new Criteria();
        criteria.and("tenant").is(serviceOptions.getCurrentTenantId());

        if (requestModel.getFilter() != null) {
            Map<String, Object> filters = requestModel.getFilter();
            filters.forEach((key, value) -> {
                if ("name".equals(key) && value != null) {
                    criteria.and("name").regex(".*" + value + ".*", "i");
                } else if ("code".equals(key) && value != null) {
                    criteria.and("code").is(value);
                } else if ("status".equals(key) && value != null) {
                    criteria.and("status").is(value);
                } else if (("productCode".equals(key) || "productId".equals(key)) && value != null) {
                    criteria.and("productCode").is(value);
                }
            });
        }

        int page = offset.orElse(0) / limit.orElse(10);
        Sort sort = orderBy.map(property -> property.isEmpty() ? Sort.unsorted() : Sort.by(Sort.Direction.DESC, property))
                .orElse(Sort.by(Sort.Direction.DESC, "createdAt"));
        PageRequest pageRequest = PageRequest.of(page, limit.orElse(10), sort);

        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, ProcessConfiguration.class);
        List<ProcessConfiguration> rows = mongoTemplate.find(query.with(pageRequest), ProcessConfiguration.class);

        List<ProcessConfigurationModel> models = new ArrayList<>();
        for (ProcessConfiguration record : rows) {
            models.add(toModel(record));
        }

        return PageableExecutionUtils.getPage(models, pageRequest, () -> count);
    }

    @Override
    public List<ProcessConfigurationModel> findAll(ServiceOptions serviceOptions) {
        List<ProcessConfiguration> records = repository.findByTenantAndStatus(serviceOptions.getCurrentTenantId(), ProcessStatus.ACTIVE);
        List<ProcessConfigurationModel> models = new ArrayList<>();
        for (ProcessConfiguration record : records) {
            models.add(toModel(record));
        }
        return models;
    }

    @Override
    public ProcessConfigurationModel find(ServiceOptions serviceOptions, String id) {
        ProcessConfiguration record = repository.findById(id).orElse(null);
        return toModel(record);
    }

    @Override
    public ProcessConfigurationModel create(ServiceOptions serviceOptions, ProcessConfiguration data) {
        if (data == null) {
            throw new IllegalArgumentException("ProcessConfiguration data cannot be null");
        }

        validator.validate(data);

        String tenant = serviceOptions.getCurrentTenantId();
        int version = data.getVersion() != null ? data.getVersion() : 1;

        if (data.getCode() != null) {
            Optional<ProcessConfiguration> existing = repository.findByTenantAndCodeAndVersion(tenant, data.getCode(), version);
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Process configuration with code '" + data.getCode() + "' and version " + version + " already exists.");
            }
        }

        data.tenant = tenant;
        data.setCreatedBy(serviceOptions.getCurrentUserId());
        data.setUpdatedBy(serviceOptions.getCurrentUserId());
        data.setVersion(version);
        if (data.getStatus() == null) {
            data.setStatus(ProcessStatus.ACTIVE);
        }

        ProcessConfiguration record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return toModel(record);
    }

    @Override
    public ProcessConfigurationModel update(ServiceOptions serviceOptions, String id, ProcessConfiguration data) {
        ProcessConfiguration record = repository.findById(id).orElse(null);
        if (record != null) {
            validator.validate(data);
            BeanUtils.copyProperties(data, record, "id", "createdAt", "createdBy", "updatedBy", "tenant", "version");
            record.setUpdatedBy(serviceOptions.getCurrentUserId());
            repository.save(record);
            createAuditLog(serviceOptions, "update", data, record);
        }
        return toModel(record);
    }

    @Override
    public ProcessConfigurationModel updateStatus(ServiceOptions serviceOptions, String id, ProcessStatus status) {
        ProcessConfiguration record = repository.findById(id).orElse(null);
        if (record != null) {
            record.setStatus(status);
            record.setUpdatedBy(serviceOptions.getCurrentUserId());
            repository.save(record);
            createAuditLog(serviceOptions, "updateStatus", null, record);
        }
        return toModel(record);
    }

    @Override
    public void delete(ServiceOptions serviceOptions, String id) {
        ProcessConfiguration record = repository.findById(id).orElse(null);
        if (record != null) {
            repository.delete(record);
            createAuditLog(serviceOptions, "delete", null, record);
        }
    }

    @Override
    public ProcessConfiguration importData(ServiceOptions serviceOptions, ProcessConfiguration data, String importHash) {
        Long count = repository.countImportHash(serviceOptions.getCurrentTenantId(), importHash);
        if (count > 0) {
            return null;
        }
        validator.validate(data);
        data.tenant = serviceOptions.getCurrentTenantId();
        data.importHash = importHash;
        ProcessConfiguration record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return record;
    }

    @Override
    public ProcessConfigurationModel seedIceCreamProcess(ServiceOptions serviceOptions) {
        try {
            // 1. Load Process DAG Configuration directly from JSON fixture
            ClassPathResource mfgResource = new ClassPathResource("data/icecream-manufacturing.json");
            ProcessConfiguration config;
            try (InputStream is = mfgResource.getInputStream()) {
                config = objectMapper.readValue(is, ProcessConfiguration.class);
            }

            // 2. Merge Node Metadata directly from JSON fixture (Labour, Costs, Consumables)
            ClassPathResource metaResource = new ClassPathResource("data/icecream-india-meta.json");
            Map<String, Object> metaMap;
            try (InputStream is = metaResource.getInputStream()) {
                metaMap = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
            }

            if (metaMap.containsKey("nodes")) {
                Map<String, Map<String, Object>> nodeMeta = (Map<String, Map<String, Object>>) metaMap.get("nodes");
                if (config.getNodes() != null) {
                    for (ProcessNode node : config.getNodes()) {
                        if (nodeMeta.containsKey(node.getId())) {
                            Map<String, Object> meta = nodeMeta.get(node.getId());
                            if (meta.containsKey("processLabour")) {
                                List<ProcessLabourRequirement> labours = objectMapper.convertValue(
                                        meta.get("processLabour"),
                                        new TypeReference<List<ProcessLabourRequirement>>() {}
                                );
                                node.setProcessLabour(labours);
                            }
                            if (meta.containsKey("consumables")) {
                                List<ProcessConsumableRequirement> consumables = objectMapper.convertValue(
                                        meta.get("consumables"),
                                        new TypeReference<List<ProcessConsumableRequirement>>() {}
                                );
                                node.setConsumables(consumables);
                            }
                            if (meta.containsKey("processCosts")) {
                                List<ProcessCostRule> costRules = objectMapper.convertValue(
                                        meta.get("processCosts"),
                                        new TypeReference<List<ProcessCostRule>>() {}
                            );
                                node.setProcessCosts(costRules);
                            }
                        }
                    }
                }
            }

            // 3. Dynamically Load & Seed Materials directly from JSON fixture (idempotent)
            ClassPathResource matResource = new ClassPathResource("data/icecream-materials.json");
            if (matResource.exists()) {
                try (InputStream is = matResource.getInputStream()) {
                    List<MaterialMaster> materials = objectMapper.readValue(is, new TypeReference<List<MaterialMaster>>() {});
                    for (MaterialMaster mat : materials) {
                        seedMaterialIfAbsent(serviceOptions, mat);
                    }
                }
            }

            // 4. Dynamically Load & Seed Machines directly from JSON fixture (idempotent)
            ClassPathResource machResource = new ClassPathResource("data/icecream-machines.json");
            if (machResource.exists()) {
                try (InputStream is = machResource.getInputStream()) {
                    List<MachineMaster> machines = objectMapper.readValue(is, new TypeReference<List<MachineMaster>>() {});
                    for (MachineMaster mach : machines) {
                        seedMachineIfAbsent(serviceOptions, mach);
                    }
                }
            }

            // 5. Idempotent Process Configuration Creation / Update
            String tenant = serviceOptions.getCurrentTenantId();
            int version = config.getVersion() != null ? config.getVersion() : 1;
            config.setVersion(version);
            config.setStatus(ProcessStatus.ACTIVE);

            Optional<ProcessConfiguration> existing = repository.findByTenantAndCodeAndVersion(tenant, config.getCode(), version);
            if (existing.isPresent()) {
                // If already seeded/created for this tenant, update it in-place instead of creating duplicates
                ProcessConfiguration existingRecord = existing.get();
                return update(serviceOptions, existingRecord.getId(), config);
            }

            return create(serviceOptions, config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to seed Ice Cream process: " + e.getMessage(), e);
        }
    }

    private void seedMaterialIfAbsent(ServiceOptions serviceOptions, MaterialMaster mat) {
        String tenant = serviceOptions.getCurrentTenantId();
        if (materialMasterRepository.findByTenantAndCode(tenant, mat.getCode()).isEmpty()) {
            mat.tenant = tenant;
            if (mat.getStatus() == null) mat.setStatus(ItemStatus.ACTIVE);
            mat.setCreatedBy(serviceOptions.getCurrentUserId());
            mat.setUpdatedBy(serviceOptions.getCurrentUserId());
            materialMasterRepository.save(mat);
        }
    }

    private void seedMachineIfAbsent(ServiceOptions serviceOptions, MachineMaster mach) {
        String tenant = serviceOptions.getCurrentTenantId();
        if (machineMasterRepository.findByTenantAndCode(tenant, mach.getCode()).isEmpty()) {
            mach.tenant = tenant;
            if (mach.getStatus() == null) mach.setStatus(ItemStatus.ACTIVE);
            mach.setCreatedBy(serviceOptions.getCurrentUserId());
            mach.setUpdatedBy(serviceOptions.getCurrentUserId());
            machineMasterRepository.save(mach);
        }
    }

    private ProcessConfigurationModel toModel(ProcessConfiguration record) {
        if (record == null) return null;
        ProcessConfigurationModel model = new ProcessConfigurationModel();
        BeanUtils.copyProperties(record, model);
        return model;
    }

    private void createAuditLog(ServiceOptions serviceOptions, String action, ProcessConfiguration data, ProcessConfiguration record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("process_configuration");
        auditLog.setAction(action);
        auditLog.setEntityId(record.getId());
        auditLog.tenant = serviceOptions.getCurrentTenantId();
        auditLog.timestamp = new Date();
        auditLog.setCreatedBy(serviceOptions.getCurrentUserId());
        auditLog.setUpdatedBy(serviceOptions.getCurrentUserId());
        if (data != null) {
            Map<String, Object> map = objectMapper.convertValue(data, Map.class);
            auditLog.setValues(map);
        }
        auditLogRepository.save(auditLog);
    }
}
