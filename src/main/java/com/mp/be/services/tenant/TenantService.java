/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.tenant;
import com.mp.be.models.tenant.TenantRequestModel;
import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.User;
import com.mp.be.services.ServiceOptions;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TenantService {

    public Tenant create(Tenant data, User currentUser);

    public Page<Tenant> findAndCountAll(ServiceOptions serviceOptions,
                                         TenantRequestModel requestModel,
                                         Optional<Integer> limit,
                                         Optional<Integer> offset,
                                         Optional <String> orderBy);

    public Tenant find(String id);

    Tenant acceptInvitation(String token, User currentUser, ServiceOptions options);
}