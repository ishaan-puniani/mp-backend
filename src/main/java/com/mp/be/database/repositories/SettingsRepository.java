/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import com.mp.be.database.entities.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface SettingsRepository extends MongoRepository<Setting,String> {
    @Query("{ 'id' : ?0 }")
    Optional<Setting> findById(String id);

    @Query("{ 'tenant' : ?0 }")
    Setting findByTenantId(String id);
}
