/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import com.mp.be.database.entities.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends MongoRepository<Tenant, String> {
    @Query("{ 'id' : ?0 }")
    Optional<Tenant> findById(String id);
}
