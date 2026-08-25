package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

    @Test
    public void testProfileConstructorAndFields() {
        List<File> avatars = List.of(new File());
        Profile profile = new Profile("tenant123", "email@example.com", "John", "Doe", "1234567890", avatars);
        assertEquals("tenant123", profile.getTenantId());
        assertEquals("email@example.com", profile.getEmail());
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals("1234567890", profile.getPhoneNumber());
        assertEquals(avatars, profile.getAvatars());
    }

    @Test
    public void testLombokGeneratedMethods() {
        Profile profile = new Profile();
        profile.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", profile.getEmail());
    }

    @Test
    public void testBoundaryConditions() {
        Profile profile = new Profile();
        profile.setEmail("");
        assertEquals("", profile.getEmail());
    }

    @Test
    public void testNullAndInvalidValues() {
        Profile profile = new Profile();
        profile.setEmail(null);
        assertNull(profile.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        Profile profile1 = new Profile("tenant123", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant123", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        assertEquals(profile1, profile2);
        assertEquals(profile1.hashCode(), profile2.hashCode());
    }

    @Test
    public void testToString() {
        Profile profile = new Profile("tenant123", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        assertNotNull(profile.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        List<File> avatars = List.of(new File());
        Profile profile1 = new Profile("tenant123", "email@example.com", "John", "Doe", "1234567890", avatars);
        Profile profile2 = new Profile("tenant456", "email@example.com", "John", "Doe", "1234567890", avatars);
        assertNotEquals(profile1, profile2);
        assertNotEquals(profile1.hashCode(), profile2.hashCode());

        assertNotEquals(profile1, null);
        assertNotEquals(profile1, new Object());
    }

    @Test
    public void testEqualsWithDifferentTenantId() {
        Profile profile1 = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant2", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        assertNotEquals(profile1, profile2);
    }

    @Test
    public void testEqualsWithDifferentEmail() {
        Profile profile1 = new Profile("tenant1", "email1@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant1", "email2@example.com", "John", "Doe", "1234567890", List.of(new File()));
        assertNotEquals(profile1, profile2);
    }

    @Test
    public void testEqualsWithDifferentFirstName() {
        Profile profile1 = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant1", "email@example.com", "Jane", "Doe", "1234567890", List.of(new File()));
        assertNotEquals(profile1, profile2);
    }

    @Test
    public void testEqualsWithDifferentLastName() {
        Profile profile1 = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant1", "email@example.com", "John", "Smith", "1234567890", List.of(new File()));
        assertNotEquals(profile1, profile2);
    }

    @Test
    public void testEqualsWithDifferentPhoneNumber() {
        Profile profile1 = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant1", "email@example.com", "John", "Doe", "0987654321", List.of(new File()));
        assertNotEquals(profile1, profile2);
    }

    @Test
    public void testEqualsWithDifferentAvatars() {
        Profile profile1 = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        Profile profile2 = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of());
        assertNotEquals(profile1, profile2);
    }

    @Test
    public void testEqualsWithNullFields() {
        Profile profile1 = new Profile(null, null, null, null, null, null);
        Profile profile2 = new Profile(null, null, null, null, null, null);
        assertEquals(profile1, profile2);
    }

    @Test
    public void testHashCodeConsistency() {
        Profile profile = new Profile("tenant1", "email@example.com", "John", "Doe", "1234567890", List.of(new File()));
        int initialHashCode = profile.hashCode();
        assertEquals(initialHashCode, profile.hashCode());
        assertEquals(initialHashCode, profile.hashCode());
    }
} 