/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.tenant;

import com.mp.be.database.entities.TenantUser;
import com.mp.be.models.tenant.TenantRequestModel;
import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.User;
import com.mp.be.database.repositories.SettingsRepository;
import com.mp.be.database.repositories.TenantRepository;
import com.mp.be.database.repositories.TenantUserRepository;
import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.services.ServiceOptions;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import java.time.format.DateTimeFormatter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TenantRepository repository;
    @Autowired
    private TenantUserRepository tenantUserRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private SettingsRepository settingRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public Tenant create(Tenant data, User currentUser) {
        data.setCreatedBy(currentUser.getId());
        data.setUpdatedBy(currentUser.getId());
        Tenant record = repository.save(data);
        createAuditLog("create", data, record);

        tenantUserRepository.create(record, currentUser, List.of("admin"));
        return  record;
    }

    @Override
    public Page<Tenant> findAndCountAll(ServiceOptions serviceOptions,
                                        TenantRequestModel requestModel,
                                        Optional<Integer> limit,
                                        Optional<Integer> offset,
                                        Optional <String> orderBy){

        // Extract tenant IDs directly from the User's tenant list
        List<String> tenantIds = serviceOptions.getCurrentUser().getTenants().stream()
                .map(tenantUser -> tenantUser.getTenant())
                .collect(Collectors.toList());

        // If there are no associated tenants, return an empty page
        if (tenantIds.isEmpty()) {
            return Page.empty();
        }

        // Create criteria to filter by tenant IDs
        Criteria criteria = Criteria.where("id").in(tenantIds);

        int page = offset.orElse(0) / limit.orElse(10);

        Sort sort = orderBy.map(property  ->property.isEmpty() ? Sort.unsorted() : Sort.by(Sort.Direction.ASC, property))
                .orElse(Sort.unsorted());
        PageRequest pageRequest = PageRequest.of(page, limit.orElse(10),sort);

        Query query = new Query(criteria);

        // Count the total number of matching documents
        long count = mongoTemplate.count(query, Tenant.class);

        // Execute the query with pagination
        List<Tenant> rows = mongoTemplate.find(query.with(pageRequest), Tenant.class);

        // Create a Page object
        return PageableExecutionUtils.getPage(rows, pageRequest, () -> count);
        
    }

    @Override
    public Tenant acceptInvitation(String invitationToken, User currentUser, ServiceOptions options) {

        TenantUser invitationTenantUser = findByInvitationToken(invitationToken, options);

        if (invitationTenantUser == null) {
            throw new IllegalArgumentException("Invitation token is invalid or expired.");
        }

        TenantUser existingTenantUser = findByTenantAndUser(invitationTenantUser.getTenant(), currentUser.getId());
        Tenant tenant = new Tenant();
        if (existingTenantUser != null && !existingTenantUser.getTenant().equals(invitationTenantUser.getTenant())) {
            destroy(invitationTenantUser.getTenant(), currentUser.getId(), options);

            mergeRoles(existingTenantUser, invitationTenantUser);

            existingTenantUser.setInvitationToken(null);
            existingTenantUser.setStatus(selectStatus("active", existingTenantUser.getRoles()));

            mongoTemplate.save(existingTenantUser);
            updateOrAddTenantUser(currentUser,existingTenantUser);
            mongoTemplate.save(currentUser);
            return repository.findById(existingTenantUser.getTenant()).orElse(null);

        } else {
            invitationTenantUser.setInvitationToken(null);
            invitationTenantUser.setStatus(selectStatus("active", invitationTenantUser.getRoles()));

            mongoTemplate.save(invitationTenantUser);
            updateOrAddTenantUser(currentUser,invitationTenantUser);
            mongoTemplate.save(currentUser);
            return repository.findById(invitationTenantUser.getTenant()).orElse(null);
        }
    }
    public void updateOrAddTenantUser(User currentUser, TenantUser tenantUserToAddOrUpdate) {
        List<TenantUser> tenants = currentUser.getTenants();

        if (tenants == null) {
            tenants = new ArrayList<>();
        }

        boolean tenantExists = tenants.stream()
                .anyMatch(tenant -> tenant.getTenant().equals(tenantUserToAddOrUpdate.getTenant()));

        if (tenantExists) {
            // Update existing tenant entry
            tenants = tenants.stream()
                    .map(tenant -> tenant.getTenant().equals(tenantUserToAddOrUpdate.getTenant()) ? tenantUserToAddOrUpdate : tenant)
                    .collect(Collectors.toList());
        } else {
            // Add new tenant if not present
            tenants.add(tenantUserToAddOrUpdate);
        }

        currentUser.setTenants(tenants);
    }

    private TenantUser findByInvitationToken(String invitationToken, ServiceOptions options) {
        Query query = new Query(Criteria.where("tenants").elemMatch(Criteria.where("invitationToken").is(invitationToken)));
        User user = mongoTemplate.findOne(query, User.class);
        if (user != null && user.getTenants() != null) {
            return user.getTenants().stream()
                    .filter(tenantUser -> invitationToken.equals(tenantUser.getInvitationToken()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private TenantUser findByTenantAndUser(String tenantId, String userId) {
        User user = mongoTemplate.findById(userId, User.class);
        if (user != null && user.getTenants() != null) {
            return user.getTenants().stream()
                    .filter(tu -> tu.getTenant().equals(tenantId))
                    .findFirst().orElse(null);
        }
        return null;
    }
    public void mergeRoles(TenantUser existingTenantUser, TenantUser invitationTenantUser) {
        Set<String> mergedRoles = new HashSet<>(existingTenantUser.getRoles());
        mergedRoles.addAll(invitationTenantUser.getRoles());
        existingTenantUser.setRoles(List.copyOf(mergedRoles));
    }

    private String selectStatus(String oldStatus, List<String> newRoles) {
        if ("invited".equals(oldStatus)) {
            return oldStatus;
        }

        if (newRoles == null || newRoles.isEmpty()) {
            return "empty-permissions";
        }

        return "active";
    }
    public void destroy(String tenantId, String userId, ServiceOptions options) {
        User user = mongoTemplate.findById(userId, User.class);
        if (user != null && user.getTenants() != null) {
            user.getTenants().removeIf(tu -> tu.getTenant().equals(tenantId));
            mongoTemplate.save(user);
        }
    }

    @Override
    public Tenant find(String id) {
        return repository.findById(id).orElse(null);
    }

    private void createAuditLog(String action, Tenant data, Tenant record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("project");
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
}