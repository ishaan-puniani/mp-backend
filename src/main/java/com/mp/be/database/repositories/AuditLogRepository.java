/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.mp.be.database.entities.AuditLog;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
	@Query("{ 'id' : ?0 }")
    Optional<AuditLog> findById(String id); 
	  
}