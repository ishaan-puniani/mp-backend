package com.mp.be.database.repositories;

import com.mp.be.database.entities.process.ProcessConfiguration;
import com.mp.be.database.enumerator.ProcessStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessConfigurationRepository extends MongoRepository<ProcessConfiguration, String> {

    @Query("{ 'tenant': ?0, 'code': ?1, 'version': ?2 }")
    Optional<ProcessConfiguration> findByTenantAndCodeAndVersion(String tenant, String code, Integer version);

    @Query("{ 'tenant': ?0, 'code': ?1 }")
    Optional<ProcessConfiguration> findByTenantAndCode(String tenant, String code);

    @Query("{ 'tenant': ?0, 'productCode': ?1, 'status': 'ACTIVE' }")
    Optional<ProcessConfiguration> findActiveByTenantAndProductCode(String tenant, String productCode);

    @Query("{ 'tenant': ?0, 'status': ?1 }")
    List<ProcessConfiguration> findByTenantAndStatus(String tenant, ProcessStatus status);

    @Query(value = "{ 'tenant': ?0, 'importHash': ?1 }", count = true)
    Long countImportHash(String tenant, String importHash);
}
