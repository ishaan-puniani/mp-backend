/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.auditLog;

import com.mp.be.database.entities.AuditLog;
import com.mp.be.models.auditLog.AuditLogRequestModel;
import com.mp.be.services.ServiceOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface AuditLogService {
	
	public Page<AuditLog> findAndCountAll(ServiceOptions serviceOptions,
                                          AuditLogRequestModel requestModel,
                                          Optional<Integer> limit,
                                          Optional<Integer> offset,
                                          Optional <String> orderBy);
 
    public AuditLog create(ServiceOptions serviceOptions, AuditLog data); 

    public AuditLog find(ServiceOptions serviceOptions, String id); 

    public void delete(ServiceOptions serviceOptions, String id );
        
}