package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileTest {

    @Test
    public void testFileConstructorAndFields() {
        File file = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        assertEquals("file1", file.getName());
        assertEquals(1024L, file.getSizeInBytes());
        assertEquals("privateUrl", file.getPrivateUrl());
        assertEquals("publicUrl", file.getPublicUrl());
        assertEquals("downloadUrl", file.getDownloadUrl());
        assertTrue(file.isIsnew());
    }

    @Test
    public void testLombokGeneratedMethods() {
        File file = new File();
        file.setName("file2");
        assertEquals("file2", file.getName());
    }

    @Test
    public void testBoundaryConditions() {
        File file = new File();
        file.setName("");
        assertEquals("", file.getName());
    }

    @Test
    public void testNullAndInvalidValues() {
        File file = new File();
        file.setName(null);
        assertNull(file.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        File file1 = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        File file2 = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        assertEquals(file1, file2);
        assertEquals(file1.hashCode(), file2.hashCode());
    }

    @Test
    public void testToString() {
        File file = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        assertNotNull(file.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        File file1 = new File("file1", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        File file2 = new File("file2", 1024L, "privateUrl", "publicUrl", "downloadUrl", true);
        assertNotEquals(file1, file2);
        assertNotEquals(file1.hashCode(), file2.hashCode());

        assertNotEquals(file1, null);
        assertNotEquals(file1, new Object());
    }
} 