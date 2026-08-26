package com.mp.be.services.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.entities.File;
import com.mp.be.database.entities.Product;
import com.mp.be.database.entities.User;
import com.mp.be.database.enumerator.ItemStatus;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.FileRepository;
import com.mp.be.database.repositories.ProductRepository;
import com.mp.be.models.file.UserAddApproversByModel;
import com.mp.be.models.product.ProductModel;
import com.mp.be.models.product.ProductRequestModel;
import com.mp.be.services.ServiceOptions;
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
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FileRepository fileRepository;

    private final ObjectMapper objectMapper;

    public ProductServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Page<ProductModel> findAndCountAll(ServiceOptions serviceOptions,
                                              ProductRequestModel requestModel,
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
                } else if ("processConfigCode".equals(key) && value != null) {
                    criteria.and("processConfigCode").is(value);
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
        long count = mongoTemplate.count(query, Product.class);
        List<Product> rows = mongoTemplate.find(query.with(pageRequest), Product.class);
        List<ProductModel> productModels = createProductResponseList(rows, serviceOptions.getCurrentUser());

        return PageableExecutionUtils.getPage(productModels, pageRequest, () -> count);
    }

    @Override
    public List<ProductModel> findAll(ServiceOptions serviceOptions) {
        User currentUser = serviceOptions.getCurrentUser();
        List<Product> products = repository.findAll();
        return createProductResponseList(products, currentUser);
    }

    @Override
    public ProductModel find(ServiceOptions serviceOptions, String id) {
        User currentUser = serviceOptions.getCurrentUser();
        Product record = repository.findById(id).orElse(null);
        return createProductResponse(record, currentUser != null ? currentUser.getEmail() : "");
    }

    private List<ProductModel> createProductResponseList(List<Product> products, User currentUser) {
        List<ProductModel> productModels = new ArrayList<>();
        String email = currentUser != null ? currentUser.getEmail() : "";
        for (Product record : products) {
            productModels.add(createProductResponse(record, email));
        }
        return productModels;
    }

    @Override
    public ProductModel create(ServiceOptions serviceOptions, Product data) {
        if (data == null) {
            throw new IllegalArgumentException("Product data cannot be null");
        }
        User currentUser = serviceOptions.getCurrentUser();
        data.tenant = serviceOptions.getCurrentTenantId();
        saveFileModels(data.getImage());
        saveFileModels(data.getUploadedFile());
        data.setCreatedBy(serviceOptions.getCurrentUserId());
        data.setUpdatedBy(serviceOptions.getCurrentUserId());
        if (data.getStatus() == null) {
            data.setStatus(ItemStatus.ACTIVE);
        }

        Product record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return createProductResponse(record, currentUser != null ? currentUser.getEmail() : "");
    }

    private ProductModel createProductResponse(Product record, String email) {
        if (record == null) return null;
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(record, productModel);
        productModel.setAddBy(new UserAddApproversByModel(record.getAddBy(), email));
        return productModel;
    }

    @Override
    public Product importData(ServiceOptions serviceOptions, Product data, String importHash) {
        Long countByImportHash = repository.countImportHash(importHash);
        if (countByImportHash > 0) {
            return null;
        }
        data.tenant = serviceOptions.getCurrentTenantId();
        data.importHash = importHash;
        if (data.getStatus() == null) {
            data.setStatus(ItemStatus.ACTIVE);
        }

        Product record = repository.save(data);
        createAuditLog(serviceOptions, "create", data, record);
        return record;
    }

    @Override
    public void delete(ServiceOptions serviceOptions, String id) {
        Product record = repository.findById(id).orElse(null);
        if (record != null) {
            repository.delete(record);
            createAuditLog(serviceOptions, "delete", null, record);
        }
    }

    @Override
    public ProductModel update(ServiceOptions serviceOptions, String id, Product data) {
        User currentUser = serviceOptions.getCurrentUser();
        Product record = repository.findById(id).orElse(null);
        if (record != null) {
            saveFileModels(data.getImage());
            saveFileModels(data.getUploadedFile());
            BeanUtils.copyProperties(data, record, "id", "image", "uploadedFile", "createdAt", "createdBy", "updatedBy", "tenant");
            repository.save(record);
            createAuditLog(serviceOptions, "update", data, record);
        }
        return createProductResponse(record, currentUser != null ? currentUser.getEmail() : "");
    }

    private void createAuditLog(ServiceOptions serviceOptions, String action, Product data, Product record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("product");
        auditLog.setAction(action);
        auditLog.setEntityId(record.id);
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

    private void saveFileModels(List<File> files) {
        if (files != null) {
            for (File file : files) {
                if (file != null && (file.id == null || fileRepository.findById(file.id).isEmpty())) {
                    file.setId(String.valueOf(new ObjectId()));
                    fileRepository.save(file);
                }
            }
        }
    }
}
