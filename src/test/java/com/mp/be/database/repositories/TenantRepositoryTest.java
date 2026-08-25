package com.mp.be.database.repositories;

import com.mp.be.database.entities.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class TenantRepositoryTest {

    @Autowired
    private TenantRepository tenantRepository;

    @Test
    void testSaveAndFindById() {
        Tenant tenant = new Tenant();
        tenantRepository.save(tenant);

        Tenant found = tenantRepository.findById(tenant.getId()).orElse(null);
        assertThat(found).isNotNull();
    }

    @Test
    void testDelete() {
        Tenant tenant = new Tenant();
        tenantRepository.save(tenant);
        tenantRepository.delete(tenant);

        Tenant found = tenantRepository.findById(tenant.getId()).orElse(null);
        assertThat(found).isNull();
    }
} 