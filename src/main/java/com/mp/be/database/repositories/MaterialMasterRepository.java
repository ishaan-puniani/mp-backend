package com.mp.be.database.repositories;

import com.mp.be.database.entities.MaterialMaster;
import com.mp.be.database.enumerator.ItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialMasterRepository extends MongoRepository<MaterialMaster, String> {

    @Query("{ 'tenant': ?0, 'code': ?1 }")
    Optional<MaterialMaster> findByTenantAndCode(String tenant, String code);

    @Query("{ 'tenant': ?0, 'status': ?1 }")
    List<MaterialMaster> findByTenantAndStatus(String tenant, ItemStatus status);

    @Query("{ 'tenant': ?0, 'status': 'ACTIVE' }")
    List<MaterialMaster> findActiveByTenant(String tenant);

    @Query(value = "{ 'tenant': ?0, 'importHash': ?1 }", count = true)
    Long countImportHash(String tenant, String importHash);
}
