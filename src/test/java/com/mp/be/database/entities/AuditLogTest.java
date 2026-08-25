package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class AuditLogTest {

    @Test
    public void testAuditLogConstructorAndFields() {
        Date now = new Date();
        Map<String, Object> values = Map.of("key", "value");
        AuditLog auditLog = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", now, values);
        assertEquals("Entity", auditLog.getEntityName());
        assertEquals("123", auditLog.getEntityId());
        assertEquals("CREATE", auditLog.getAction());
        assertEquals("tenant123", auditLog.getTenantId());
        assertEquals("user123", auditLog.getCreatedById());
        assertEquals("user@example.com", auditLog.getCreatedByEmail());
        assertEquals(now, auditLog.getTimestamp());
        assertEquals(values, auditLog.getValues());
    }

    @Test
    public void testLombokGeneratedMethods() {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("NewEntity");
        assertEquals("NewEntity", auditLog.getEntityName());
    }

    @Test
    public void testBoundaryConditions() {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName("");
        assertEquals("", auditLog.getEntityName());
    }

    @Test
    public void testNullAndInvalidValues() {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName(null);
        assertNull(auditLog.getEntityName());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date now = new Date();
        Map<String, Object> values = Map.of("key", "value");
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", now, values);
        AuditLog auditLog2 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", now, values);
        assertEquals(auditLog1, auditLog2);
        assertEquals(auditLog1.hashCode(), auditLog2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentFields() {
        Date now = new Date();
        Map<String, Object> values1 = Map.of("key1", "value1");
        Map<String, Object> values2 = Map.of("key2", "value2");
        AuditLog auditLog1 = new AuditLog("Entity1", "123", "CREATE", "tenant123", "user123", "user1@example.com", now, values1);
        AuditLog auditLog2 = new AuditLog("Entity2", "456", "UPDATE", "tenant456", "user456", "user2@example.com", now, values2);
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithNullFields() {
        AuditLog auditLog1 = new AuditLog(null, null, null, null, null, null, null, null);
        AuditLog auditLog2 = new AuditLog(null, null, null, null, null, null, null, null);
        assertEquals(auditLog1, auditLog2);
    }

    @Test
    public void testHashCodeConsistency() {
        Date now = new Date();
        Map<String, Object> values = Map.of("key", "value");
        AuditLog auditLog = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", now, values);
        int initialHashCode = auditLog.hashCode();
        assertEquals(initialHashCode, auditLog.hashCode());
        assertEquals(initialHashCode, auditLog.hashCode());
    }

    @Test
    public void testToString() {
        AuditLog auditLog = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        assertNotNull(auditLog.toString());
    }

    @Test
    public void testEqualsWithDifferentEntityName() {
        AuditLog auditLog1 = new AuditLog("Entity1", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity2", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentEntityId() {
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity", "456", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentAction() {
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity", "123", "UPDATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentTenantId() {
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity", "123", "CREATE", "tenant456", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentCreatedById() {
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user456", "user@example.com", new Date(), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentCreatedByEmail() {
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user1@example.com", new Date(), Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user2@example.com", new Date(), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentTimestamp() {
        Date now = new Date();
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", now, Map.of("key", "value"));
        AuditLog auditLog2 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(now.getTime() + 1000), Map.of("key", "value"));
        assertNotEquals(auditLog1, auditLog2);
    }

    @Test
    public void testEqualsWithDifferentValues() {
        AuditLog auditLog1 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key1", "value1"));
        AuditLog auditLog2 = new AuditLog("Entity", "123", "CREATE", "tenant123", "user123", "user@example.com", new Date(), Map.of("key2", "value2"));
        assertNotEquals(auditLog1, auditLog2);
    }

} 