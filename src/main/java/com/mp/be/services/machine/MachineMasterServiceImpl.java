package com.mp.be.services.machine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.entities.MachineMaster;
import com.mp.be.database.enumerator.ItemStatus;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.MachineMasterRepository;
import com.mp.be.models.machine.MachineMasterModel;
import com.mp.be.models.machine.MachineMasterRequestModel;
import com.mp.be.services.ServiceOptions;
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
public class MachineMasterServiceImpl implements MachineMasterService {

    @Autowired
    private MachineMasterRepository repository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    public MachineMasterServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Page<MachineMasterModel> findAndCountAll(ServiceOptions serviceOptions,
                                                    MachineMasterRequestModel requestModel,
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
                } else if ("shopName".equals(key) && value != null) {
                    criteria.and("shopName").regex(".*" + value + ".*", "i");
                } else if ("status".equals(key) && value != null) {
                    ItemStatus status = ItemStatus.fromValue(value.toString());
                    if (status != null) {
                        criteria.and("status").is(status.name());
                    }
                }
            });
        }

        int page = offset.orElse(0) / limit.orElse(10);
        Sort sort = orderBy.map(property -> property.isEmpty() ? Sort.unsorted() : Sort.by(Sort.Direction.ASC, property))
                .orElse(Sort.unsorted());
        PageRequest pageRequest = PageRequest.of(page, limit.orElse(10), sort);

        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, MachineMaster.class);
        List<MachineMaster> rows = mongoTemplate.find(query.with(pageRequest), MachineMaster.class);

        List<MachineMasterModel> models = new ArrayList<>();
        for (MachineMaster record : rows) {
            models.add(toModel(record));
        }

        return PageableExecutionUtils.getPage(models, pageRequest, () -> count);
    }

    @Override
    public List<MachineMasterModel> findAll(ServiceOptions serviceOptions) {
        List<MachineMaster> records = repository.findActiveByTenant(serviceOptions.getCurrentTenantId());
        List<MachineMasterModel> models = new ArrayList<>();
        for (MachineMaster record : records) {
            models.add(toModel(record));
        }
        return models;
    }

    @Override
    public MachineMasterModel find(ServiceOptions serviceOptions, String id) {
        MachineMaster record = repository.findById(id).orElse(null);
        return toModel(record);
    }

    @Override
    public MachineMasterModel create(ServiceOptions serviceOptions, MachineMaster data) {
        if (data == null) {
            throw new IllegalArgumentException("Machine data cannot be null");
        }
        data.tenant = serviceOptions.getCurrentTenantId();
        data.setCreatedBy(serviceOptions.getCurrentUserId());
        data.setUpdatedBy(serviceOptions.getCurrentUserId());
        if (data.getStatus() == null) {
            data.setStatus(ItemStatus.ACTIVE);
        }

        MachineMaster record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return toModel(record);
    }

    @Override
    public MachineMasterModel update(ServiceOptions serviceOptions, String id, MachineMaster data) {
        MachineMaster record = repository.findById(id).orElse(null);
        if (record != null) {
            BeanUtils.copyProperties(data, record, "id", "createdAt", "createdBy", "updatedBy", "tenant");
            record.setUpdatedBy(serviceOptions.getCurrentUserId());
            repository.save(record);
            createAuditLog(serviceOptions, "update", data, record);
        }
        return toModel(record);
    }

    @Override
    public void delete(ServiceOptions serviceOptions, String id) {
        MachineMaster record = repository.findById(id).orElse(null);
        if (record != null) {
            repository.delete(record);
            createAuditLog(serviceOptions, "delete", null, record);
        }
    }

    @Override
    public MachineMaster importData(ServiceOptions serviceOptions, MachineMaster data, String importHash) {
        Long count = repository.countImportHash(serviceOptions.getCurrentTenantId(), importHash);
        if (count > 0) {
            return null;
        }
        data.tenant = serviceOptions.getCurrentTenantId();
        data.importHash = importHash;
        if (data.getStatus() == null) {
            data.setStatus(ItemStatus.ACTIVE);
        }
        MachineMaster record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return record;
    }

    private MachineMasterModel toModel(MachineMaster record) {
        if (record == null) return null;
        MachineMasterModel model = new MachineMasterModel();
        BeanUtils.copyProperties(record, model);
        return model;
    }

    private void createAuditLog(ServiceOptions serviceOptions, String action, MachineMaster data, MachineMaster record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("machine");
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
