/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.user;

import com.mp.be.database.entities.*;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.TenantRepository;
import com.mp.be.database.repositories.TenantUserRepository;
import com.mp.be.database.repositories.UserRepository;
import com.mp.be.models.auth.TenantUserOption;
import com.mp.be.models.settings.SettingsModel;
import com.mp.be.models.tenant.TenantModel;
import com.mp.be.models.tenant.TenantUserModel;
import com.mp.be.models.user.UserModel;
import com.mp.be.services.BrevoEmailService;
import com.mp.be.services.ServiceOptions;
import com.mp.be.models.user.UserRequestModel;
import com.google.gson.Gson;
import com.mongodb.DBRef;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BrevoEmailService emailService;

    @Autowired
    TenantRepository tenantRepository;

    @Override
    public Page<UserModel> findAndCountAll(ServiceOptions serviceOptions,
                                      UserRequestModel requestModel,
                                      Optional<Integer> limit,
                                      Optional<Integer> offset,
                                      Optional<String> orderBy) {
        Criteria criteria = new Criteria();
        Criteria userTenantCriteria = new Criteria();
        userTenantCriteria.and("tenant").is(serviceOptions.getCurrentTenantId());
        ;
//        criteria.and("tenants").elemMatch();
        // .is(serviceOptions.getCurrentTenantId());

        if (requestModel.getFilter() != null) {
            Map<String, Object> filters = requestModel.getFilter();

            filters.forEach((key, value) -> {
                switch (key) {

                    case "email":
                        String email = (String) value;
                        if (!email.isEmpty()) {
                            criteria.and("email").regex(".*" + email + ".*", "i");
                        }
                        break;
                    case "name":
                        String name = (String) value;
                        if (!name.isEmpty()) {
                            criteria.and("name").regex(".*" + name + ".*", "i");
                        }
                        break;
                    case "status":
                        String status = (String) value;
                        if (!status.isEmpty()) {
                            userTenantCriteria.and("status").is(status);
                        }
                        break;
                    case "role":
                        String role = (String) value;
                        if (!role.isEmpty()) {
                            userTenantCriteria.and("roles").is(role);
                        }
                        break;

                    default:
                        // Handle unknown keys or ignore them
                        break;
                }
            });

        }
        criteria.and("tenants").elemMatch(userTenantCriteria);
        int page = offset.orElse(0) / limit.orElse(10);

        Sort sort = orderBy.map(property -> property.isEmpty() ? Sort.unsorted() : Sort.by(Sort.Direction.ASC, property))
                .orElse(Sort.unsorted());
        PageRequest pageRequest = PageRequest.of(page, limit.orElse(10), sort);

        Query query = new Query(criteria);

        // Count the total number of matching documents
        long count = mongoTemplate.count(query, User.class);

        // Execute the query with pagination
        List<User> rows = mongoTemplate.find(query.with(pageRequest), User.class);

//        List<User> usersw =  pagedUsers.getContent();
        List<UserModel> users = mapUserForTenantForRows(rows, serviceOptions.getCurrentTenant());


        // Create a Page object
        return PageableExecutionUtils.getPage(users, pageRequest, () -> count);

    }

    @Override
    public UserModel find(ServiceOptions serviceOptions, String id) {
        User record = repository.findById(id).orElse(null);
        UserModel userModel = mapUserForTenant(record, serviceOptions.getCurrentTenant());
        
        // Fetch full tenant information
        if (record.getTenants() != null) {
            List<TenantUserModel> tenantUserModels = new ArrayList<>();
            for (TenantUser tenantUser : record.getTenants()) {
                Tenant tenant = tenantRepository.findById(tenantUser.getTenant()).orElse(null);
                if (tenant != null) {
                    TenantModel tenantModel = new TenantModel();
                    tenantModel.setId(tenant.getId());
                    tenantModel.setName(tenant.getName());
                    tenantModel.setUrl(tenant.getUrl());
                    tenantModel.setPlan(tenant.getPlan());
                    tenantModel.setPlanStatus(tenant.getPlanStatus());
                    tenantModel.setCreatedBy(tenant.getCreatedBy());
                    tenantModel.setUpdatedBy(tenant.getUpdatedBy());
                    tenantModel.setCreatedAt(tenant.getCreatedAt());
                    tenantModel.setUpdatedAt(tenant.getUpdatedAt());
                    Setting setting = tenant.getSettings();
                    if(setting!=null) {
                        tenantModel.setSettings(new SettingsModel(setting.id, setting.theme, setting.getLogos(), setting.getBackgroundImages(), setting.getCreatedAt(), setting.getUpdatedAt(), setting.getCreatedBy(), setting.getUpdatedBy()));
                    }
                    TenantUserModel tenantUserModel = new TenantUserModel();
                    tenantUserModel.setId(tenantUser.getId());
                    tenantUserModel.setRoles(tenantUser.getRoles());
                    tenantUserModel.setTenant(tenantModel);
                    tenantUserModel.setStatus(tenantUser.getStatus());
                    tenantUserModel.setUpdatedAt(tenantUser.getUpdatedAt());
                    tenantUserModel.setCreatedAt(tenantUser.getCreatedAt());
                    tenantUserModel.setInvitationToken(tenantUser.getInvitationToken());
                    tenantUserModels.add(tenantUserModel);
                }
            }
            userModel.setTenants(tenantUserModels);
        }
        
        return userModel;
    }

    public UserModel findByEmail(ServiceOptions serviceOptions, String email) {
        User record = repository.findByEmail(email).orElse(null);
        if(record == null) {
            return null;
        }
        return mapUserForTenant(record, serviceOptions.getCurrentTenant());
    }

    @Override
    public UserModel create(ServiceOptions serviceOptions, UserModel data) {
        User existingUser = repository.findByEmail(data.email).orElse(null);
        User record = null;
        if(existingUser==null){
            User user = new User();
            user.setEmail(data.email);
            record = repository.save(user);
        }else{
            record =existingUser;
        }
        TenantUserOption tenantUserOption =  new TenantUserOption();
        tenantUserOption.isAddRoles=true;
        TenantUser tenantUser = tenantUserRepository.updateRoles(serviceOptions.getCurrentTenantId(), record.getId(), data.roles,tenantUserOption);

        if (record.getTenants() == null) {
            record.setTenants(new ArrayList<>());
        }

        boolean tenantExists = record.getTenants().stream()
                .anyMatch(tu -> Objects.equals(tu.getTenant(), serviceOptions.getCurrentTenantId()));

        if (!tenantExists) {
            record.getTenants().add(tenantUser);
        }

        String link = "http://localhost:3000/auth/invitation?token=" + tenantUser.getInvitationToken();

        // Send welcome email
        Map<String, Object> variables = new HashMap<>();
        variables.put("link", link);
        variables.put("userName", data.email);
        emailService.sendEmail(data.email, "19", variables);
        return mapUserForTenant(record, serviceOptions.getCurrentTenant());
    }

    @Override
    public void delete(String id) {
        User record = repository.findById(id).orElse(null);
        repository.delete(record);
        createAuditLog("delete", null, record);
    }

    @Override
    public UserModel update(ServiceOptions serviceOptions, String id, UserModel data) {
        User record = repository.findById(id).orElse(null);
        if (record != null) {
            // Copy properties from 'data' to 'record', ignoring null values
            BeanUtils.copyProperties(data, record, "id");

            // Save the updated record
            repository.save(record);
            createAuditLog("update", data, record);

            tenantUserRepository.updateRoles(serviceOptions.getCurrentTenantId(), id, data.roles, new TenantUserOption());
        }
        return mapUserForTenant(record, serviceOptions.getCurrentTenant());
    }

    @Override
    public List<User> findAll(ServiceOptions serviceOptions) {
        List<User> users = repository.findAll();
       return users;
    }
    /**
     * Maps the users data to show only the current tenant related info
     */
    public static List<UserModel> mapUserForTenantForRows(List<User> rows, Tenant tenant) {
        if (rows == null) {
            return null;
        }

        List<UserModel> users = new ArrayList<>();
        for (User record : rows) {
            users.add(mapUserForTenant(record, tenant));
        }
        return users;
    }

    /**
     * Maps the user data to show only the current tenant related info
     */
    public static UserModel mapUserForTenant(User user, Tenant tenant) {

        TenantUser tenantUser = null;
        if(tenant!=null) {
            for (TenantUser tu : user.getTenants()) {
                if (tu != null && tu.getTenant() != null && String.valueOf(tu.getTenant()).equals(String.valueOf(tenant.id))) {
                    tenantUser = tu;
                    break;
                }
            }
        }

        String status = tenantUser != null ? tenantUser.getStatus() : null;
        List<String> roles = tenantUser != null ? tenantUser.getRoles() : new ArrayList<>();

        UserModel otherData = new UserModel();

        otherData.id = user.getId();
        otherData.email = user.getEmail();
        otherData.roles = roles;
        otherData.status = status;
        otherData.emailVerified = user.getEmailVerified();
        otherData.setTenants(new ArrayList<>());
        return otherData;
    }

    private void createAuditLog(String action, UserModel data, User record) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("user");
        auditLog.setAction(action);
        auditLog.setEntityId(record.getId());

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
