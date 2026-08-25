/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.TenantUser;
import com.mp.be.database.entities.User;
import com.mp.be.models.auth.TenantUserOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TenantUserRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void create(
            Tenant tenant,
            User user,
            List<String> roles

    ) {

//       User usr =  userRepository.findById(user.getId()).get();
//       String email = usr.getEmail();


        // Find the existing user by ID
        User existingUser = mongoTemplate.findById(user.getId(), User.class);

        // Check if the user exists
        if (existingUser != null) {
            // Update the existing user with the new data
//               BeanUtils.copyProperties(newUser, existingUser, "id");
            TenantUser tUser = new TenantUser();
            tUser.setRoles(roles);
            tUser.setTenant(tenant.id);
            tUser.setStatus(selectStatus("active", roles));
            mongoTemplate.save(tUser);
            if (existingUser.getTenants() == null) {
                existingUser.setTenants(List.of(tUser));
            } else {
                existingUser.getTenants().add(tUser);
            }
            // Save the updated user
            mongoTemplate.save(existingUser);

        }
    }



    public TenantUser updateRoles(String tenantId, String id, List<String> roles, TenantUserOption options) {
        User user = userRepository.findById(id).orElse(null);
        Tenant tenant = tenantRepository.findById(tenantId).orElse(null);

        TenantUser tenantUser = null;
        if(user.getTenants()!=null) {
            for (TenantUser tu : user.getTenants()) {
                if (tu != null && tu.getTenant() != null && tu.getTenant().equals(tenantId)) {
                    tenantUser = tu;
                    break;
                }
            }
        }else{
            user.setTenants(new ArrayList<>());
        }
        boolean isCreation = false;

        if (tenantUser == null) {
            isCreation = true;
            tenantUser = new TenantUser();
            tenantUser.setTenant(tenant.id);
            tenantUser.setStatus(selectStatus("invited", new ArrayList<>()));
            tenantUser.setInvitationToken(generateRandomHex());
            tenantUser.setRoles(new ArrayList<>());
            user.getTenants().add(tenantUser);
            mongoTemplate.save(user);
        }

        List<String> existingRoles = tenantUser.getRoles();
        List<String> newRoles;

        if (options.isAddRoles) {
            Set<String> roleSet = new HashSet<>(existingRoles);
            roleSet.addAll(roles);
            newRoles = new ArrayList<>(roleSet);
        } else if (options.isRemoveOnlyInformedRoles) {
            newRoles = existingRoles.stream()
                    .filter(existingRole -> !roles.contains(existingRole))
                    .collect(Collectors.toList());
        } else {
            newRoles = roles != null ? roles : new ArrayList<>();
        }

        tenantUser.setRoles(newRoles);
        tenantUser.setStatus(selectStatus(tenantUser.getStatus(), newRoles));
        mongoTemplate.save(tenantUser);
//        User(options.getDatabase()).updateOne(
//                and(eq("_id", id), eq("tenants.tenant", tenantId)),
//                new Document("$set", new Document("tenants.$.roles", newRoles)
//                        .append("tenants.$.status", tenantUser.getStatus())),
//                options
//        );




//        AuditLogRepository.log(
//                new AuditLog("user", user.getId(), isCreation ? AuditLogRepository.CREATE : AuditLogRepository.UPDATE,
//                        new AuditLogValues(user.getEmail(), tenantUser.getStatus(), newRoles)),
//                options
//        );

       mongoTemplate.save(user);
        return tenantUser;
    }


       public String selectStatus(String oldStatus, List<String> newRoles) {
             newRoles = newRoles != null ? newRoles : new ArrayList<>();

           if ("invited".equals(oldStatus)) {
               return oldStatus;
           }

           if (newRoles.isEmpty()) {
               return "empty-permissions";
           }

           return "active";
       }

    private  String generateRandomHex() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

//       static async updateRoles(tenantId, id, roles, options) {
//    const user =
//                   await MongooseRepository.wrapWithSessionIfExists(
//                   User(options.database)
//                           .findById(id)
//                           .populate('tenants.tenant'),
//                   options,
//                   );
//
//           let tenantUser = user.tenants.find((userTenant) => {
//           return userTenant.tenant.id === tenantId;
//    });
//
//           let isCreation = false;
//
//           if (!tenantUser) {
//               isCreation = true;
//               tenantUser = {
//                       tenant: tenantId,
//                       status: selectStatus('invited', []),
//               invitationToken: crypto
//                       .randomBytes(20)
//                       .toString('hex'),
//                       roles: [],
//      };
//
//               await User(options.database).updateOne(
//                       { _id: id },
//               {
//                   $push: {
//                       tenants: tenantUser,
//                   },
//               },
//               options,
//      );
//           }
//
//           let { roles: existingRoles } = tenantUser;
//
//           let newRoles = [] as Array<string>;
//
//           if (options.addRoles) {
//               newRoles = [...new Set([...existingRoles, ...roles])];
//           } else if (options.removeOnlyInformedRoles) {
//               newRoles = existingRoles.filter(
//                       (existingRole) => !roles.includes(existingRole),
//      );
//           } else {
//               newRoles = roles || [];
//           }
//
//           tenantUser.roles = newRoles;
//           tenantUser.status = selectStatus(
//                   tenantUser.status,
//                   newRoles,
//                   );
//
//           await User(options.database).updateOne(
//                   { _id: id, 'tenants.tenant': tenantId },
//           {
//               $set: {
//                   'tenants.$.roles': newRoles,
//                           'tenants.$.status': tenantUser.status,
//               },
//           },
//           options,
//    );
//
//           await AuditLogRepository.log(
//                   {
//                           entityName: 'user',
//                   entityId: user.id,
//                   action: isCreation
//                   ? AuditLogRepository.CREATE
//                   : AuditLogRepository.UPDATE,
//                   values: {
//               email: user.email,
//                       status: tenantUser.status,
//                       roles: newRoles,
//           },
//      },
//           options,
//    );
//
//           return tenantUser;
//       }


    }
