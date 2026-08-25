/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.order;

import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.entities.Order;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.OrderRepository;
import com.mp.be.models.order.OrderRequestModel;
import com.mp.be.models.order.OrderModel;
import com.mp.be.services.ServiceOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository repository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Order> findAndCountAll(ServiceOptions serviceOptions,
                                         OrderRequestModel requestModel,
                                         Optional<Integer> limit,
                                         Optional<Integer> offset,
                                         Optional <String> orderBy) {
        Criteria criteria = new Criteria();
        criteria.and("tenant").is(serviceOptions.getCurrentTenantId());

        if(requestModel.getFilter()!=null){
            Map<String, Object> filters = requestModel.getFilter();

            filters.forEach((key, value) -> {
                switch (key) {


                    default:
                        // Handle unknown keys or ignore them
                        break;
                }
            });

        }

        int page = offset.orElse(0) / limit.orElse(10);

        Sort sort = orderBy.map(property -> property.isEmpty() ? Sort.unsorted() : Sort.by(Sort.Direction.ASC, property))
                .orElse(Sort.unsorted());
        PageRequest pageRequest = PageRequest.of(page, limit.orElse(10),sort);

        Query query = new Query(criteria);
        
        // Count the total number of matching documents
        long count = mongoTemplate.count(query, Order.class);

        // Execute the query with pagination
        List<Order> rows = mongoTemplate.find(query.with(pageRequest), Order.class);

        // Create a Page object
        return PageableExecutionUtils.getPage(rows, pageRequest, () -> count);

    }

    @Override
    public List<Order> findAll(ServiceOptions serviceOptions) {
        return repository.findAll();
    }

    @Override
    public Order find(ServiceOptions serviceOptions, String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Order create(ServiceOptions serviceOptions, Order data) {
        data.tenant = serviceOptions.getCurrentTenantId();
        data.setCreatedBy(serviceOptions.getCurrentUserId());
        data.setUpdatedBy(serviceOptions.getCurrentUserId());
        Order record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return record;
    }

    public Order importData(ServiceOptions serviceOptions, Order data, String importHash){

        Long countByImportHash =  repository.countImportHash(importHash);

        if (countByImportHash > 0) {
            return null;
        }

        data.tenant = serviceOptions.getCurrentTenantId();
        data.importHash = importHash;

        Order record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return record;
    }


    @Override
    public void delete(ServiceOptions serviceOptions, String id) {
        Order record = repository.findById(id).orElse(null);
        repository.delete(record);
        createAuditLog(serviceOptions, "delete", null, record);
    }

    @Override
    public Order update(ServiceOptions serviceOptions, String id, Order data) {
        Order record = repository.findById(id).orElse(null);
        if (record != null) {
            // Copy properties from 'data' to 'record', ignoring null values
            BeanUtils.copyProperties(data, record, "id","createdAt","createdBy","updatedBy","tenant");
            // Save the updated record
            repository.save(record);
            createAuditLog(serviceOptions, "update", data, record);
        }
        return record;
    }
    
    private void createAuditLog(ServiceOptions serviceOptions, String action, Order data, Order record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("order");
        auditLog.setAction(action);
        auditLog.setEntityId(record.id);
        auditLog.tenant = serviceOptions.getCurrentTenantId();
        auditLog.timestamp = new Date();
        auditLog.setCreatedBy(serviceOptions.getCurrentUserId());
        auditLog.setUpdatedBy(serviceOptions.getCurrentUserId());
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
}
