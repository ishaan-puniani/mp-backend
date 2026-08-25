package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class BaseEntityTest {

    @Test
    public void testFieldAccessors() {
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId("123");
        assertEquals("123", baseEntity.getId());

        baseEntity.setCreatedBy("creator");
        assertEquals("creator", baseEntity.getCreatedBy());

        baseEntity.setUpdatedBy("updater");
        assertEquals("updater", baseEntity.getUpdatedBy());

        baseEntity.setTenant("tenant1");
        assertEquals("tenant1", baseEntity.getTenant());

        Date now = new Date();
        baseEntity.setCreatedAt(now);
        assertEquals(now, baseEntity.getCreatedAt());

        baseEntity.setUpdatedAt(now);
        assertEquals(now, baseEntity.getUpdatedAt());

        baseEntity.setImportHash("hash123");
        assertEquals("hash123", baseEntity.getImportHash());
    }

    @Test
    public void testEqualsAndHashCode() {
        BaseEntity baseEntity1 = new BaseEntity();
        baseEntity1.setId("123");
        BaseEntity baseEntity2 = new BaseEntity();
        baseEntity2.setId("123");
        assertEquals(baseEntity1, baseEntity2);
        assertEquals(baseEntity1.hashCode(), baseEntity2.hashCode());
    }

    @Test
    public void testToString() {
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId("123");
        assertNotNull(baseEntity.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        BaseEntity baseEntity1 = new BaseEntity();
        baseEntity1.setId("123");
        BaseEntity baseEntity2 = new BaseEntity();
        baseEntity2.setId("456");
        assertNotEquals(baseEntity1, baseEntity2);
        assertNotEquals(baseEntity1.hashCode(), baseEntity2.hashCode());

        assertNotEquals(baseEntity1, null);
        assertNotEquals(baseEntity1, new Object());
    }

    @Test
    public void testEqualsWithAllFieldsDifferent() {
        BaseEntity baseEntity1 = new BaseEntity();
        baseEntity1.setId("123");
        baseEntity1.setCreatedBy("creator1");
        baseEntity1.setUpdatedBy("updater1");
        baseEntity1.setTenant("tenant1");
        baseEntity1.setCreatedAt(new Date(1000));
        baseEntity1.setUpdatedAt(new Date(2000));
        baseEntity1.setImportHash("hash1");

        BaseEntity baseEntity2 = new BaseEntity();
        baseEntity2.setId("456");
        baseEntity2.setCreatedBy("creator2");
        baseEntity2.setUpdatedBy("updater2");
        baseEntity2.setTenant("tenant2");
        baseEntity2.setCreatedAt(new Date(3000));
        baseEntity2.setUpdatedAt(new Date(4000));
        baseEntity2.setImportHash("hash2");

        assertNotEquals(baseEntity1, baseEntity2);
    }

    @Test
    public void testHashCodeConsistency() {
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId("123");
        baseEntity.setCreatedBy("creator");
        baseEntity.setUpdatedBy("updater");
        baseEntity.setTenant("tenant1");
        baseEntity.setCreatedAt(new Date(1000));
        baseEntity.setUpdatedAt(new Date(2000));
        baseEntity.setImportHash("hash1");

        int initialHashCode = baseEntity.hashCode();
        assertEquals(initialHashCode, baseEntity.hashCode());
        assertEquals(initialHashCode, baseEntity.hashCode());
    }

    @Test
    public void testEqualsWithNullFields() {
        BaseEntity baseEntity1 = new BaseEntity();
        baseEntity1.setId(null);
        baseEntity1.setCreatedBy(null);
        baseEntity1.setUpdatedBy(null);
        baseEntity1.setTenant(null);
        baseEntity1.setCreatedAt(null);
        baseEntity1.setUpdatedAt(null);
        baseEntity1.setImportHash(null);

        BaseEntity baseEntity2 = new BaseEntity();
        baseEntity2.setId(null);
        baseEntity2.setCreatedBy(null);
        baseEntity2.setUpdatedBy(null);
        baseEntity2.setTenant(null);
        baseEntity2.setCreatedAt(null);
        baseEntity2.setUpdatedAt(null);
        baseEntity2.setImportHash(null);

        assertEquals(baseEntity1, baseEntity2);
    }

    @Test
    public void testEqualsWithDifferentId() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setId("1");
        entity2.setId("2");
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsWithDifferentCreatedBy() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setCreatedBy("user1");
        entity2.setCreatedBy("user2");
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsWithDifferentUpdatedBy() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setUpdatedBy("user1");
        entity2.setUpdatedBy("user2");
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsWithDifferentTenant() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setTenant("tenant1");
        entity2.setTenant("tenant2");
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsWithDifferentCreatedAt() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setCreatedAt(new Date(1000));
        entity2.setCreatedAt(new Date(2000));
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsWithDifferentUpdatedAt() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setUpdatedAt(new Date(1000));
        entity2.setUpdatedAt(new Date(2000));
        assertNotEquals(entity1, entity2);
    }

    @Test
    public void testEqualsWithDifferentImportHash() {
        BaseEntity entity1 = new BaseEntity();
        BaseEntity entity2 = new BaseEntity();
        entity1.setImportHash("hash1");
        entity2.setImportHash("hash2");
        assertNotEquals(entity1, entity2);
    }
} 