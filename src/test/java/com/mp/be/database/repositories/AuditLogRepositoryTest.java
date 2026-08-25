package com.mp.be.database.repositories;

import com.mp.be.database.entities.AuditLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testSaveAndFindById() {
        AuditLog auditLog = new AuditLog("Entity", "123", "CREATE", "tenantId", "userId", "user@example.com", new Date(), new HashMap<>());
        auditLogRepository.save(auditLog);

        AuditLog found = auditLogRepository.findById(auditLog.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getEntityId()).isEqualTo("123");
    }

    @Test
    void testDelete() {
        AuditLog auditLog = new AuditLog("Entity", "123", "CREATE", "tenantId", "userId", "user@example.com", new Date(), new HashMap<>());
        auditLogRepository.save(auditLog);
        auditLogRepository.delete(auditLog);

        AuditLog found = auditLogRepository.findById(auditLog.getId()).orElse(null);
        assertThat(found).isNull();
    }
} 