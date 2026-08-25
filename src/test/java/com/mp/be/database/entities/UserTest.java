package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("1");
        assertNotNull(user);
        assertEquals("1", user.getId());
    }

    @Test
    public void testEmailVerification() {
        User user = new User();
        user.setEmailVerified(true);
        assertTrue(user.getEmailVerified());
    }

    @Test
    public void testGetAuthorities() {
        User user = new User();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testUserDetailsMethods() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testBoundaryConditions() {
        User user = new User();
        user.setEmailVerified(true);
        assertTrue(user.getEmailVerified());
    }

    @Test
    public void testNullAndInvalidValues() {
        User user = new User();
        user.setEmail(null);
        assertNull(user.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("1", "test@example.com", "password", null, false, null, null, null, null);
        User user2 = new User("1", "test@example.com", "password", null, false, null, null, null, null);
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentFields() {
        User user1 = new User("1", "test1@example.com", "password1", null, false, null, null, null, null);
        User user2 = new User("2", "test2@example.com", "password2", null, true, null, null, null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithNullFields() {
        User user1 = new User(null, null, null, null, null, null, null, null, null);
        User user2 = new User(null, null, null, null, null, null, null, null, null);
        assertEquals(user1, user2);
    }

    @Test
    public void testHashCodeConsistency() {
        User user = new User("1", "test@example.com", "password", null, false, null, null, null, null);
        int initialHashCode = user.hashCode();
        assertEquals(initialHashCode, user.hashCode());
        assertEquals(initialHashCode, user.hashCode());
    }

    @Test
    public void testToString() {
        User user = new User("1", "test@example.com", "password", null, false, null, null, null, null);
        assertNotNull(user.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        User user1 = new User("1", "test@example.com", "password", null, false, null, null, null, null);
        User user2 = new User("2", "test@example.com", "password", null, false, null, null, null, null);
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());

        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());
    }

    @Test
    public void testEqualsWithDifferentEmail() {
        User user1 = new User("1", "test1@example.com", "password", null, false, null, null, null, null);
        User user2 = new User("1", "test2@example.com", "password", null, false, null, null, null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentPassword() {
        User user1 = new User("1", "test@example.com", "password1", null, false, null, null, null, null);
        User user2 = new User("1", "test@example.com", "password2", null, false, null, null, null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentEmailVerified() {
        User user1 = new User("1", "test@example.com", "password", null, false, null, null, null, null);
        User user2 = new User("1", "test@example.com", "password", null, true, null, null, null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentPasswordResetToken() {
        User user1 = new User("1", "test@example.com", "password", null, false, "token1", null, null, null);
        User user2 = new User("1", "test@example.com", "password", null, false, "token2", null, null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentPasswordResetTokenExpiresAt() {
        Instant now = Instant.now();
        User user1 = new User("1", "test@example.com", "password", null, false, null, now, null, null);
        User user2 = new User("1", "test@example.com", "password", null, false, null, now.plusSeconds(60), null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentEmailVerificationToken() {
        User user1 = new User("1", "test@example.com", "password", null, false, null, null, "token1", null);
        User user2 = new User("1", "test@example.com", "password", null, false, null, null, "token2", null);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentEmailVerificationTokenExpiresAt() {
        Instant now = Instant.now();
        User user1 = new User("1", "test@example.com", "password", null, false, null, null, null, now);
        User user2 = new User("1", "test@example.com", "password", null, false, null, null, null, now.plusSeconds(60));
        assertNotEquals(user1, user2);
    }
}