package com.mp.be.database.repositories;

import com.mp.be.database.entities.MachineMaster;
import com.mp.be.database.enumerator.ItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineMasterRepository extends MongoRepository<MachineMaster, String> {

    @Query("{ 'tenant': ?0, 'code': ?1 }")
    Optional<MachineMaster> findByTenantAndCode(String tenant, String code);

    @Query("{ 'tenant': ?0, 'status': ?1 }")
    List<MachineMaster> findByTenantAndStatus(String tenant, ItemStatus status);

    @Query("{ 'tenant': ?0, 'status': 'ACTIVE' }")
    List<MachineMaster> findActiveByTenant(String tenant);

    @Query(value = "{ 'tenant': ?0, 'importHash': ?1 }", count = true)
    Long countImportHash(String tenant, String importHash);
}
