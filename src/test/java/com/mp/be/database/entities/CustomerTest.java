package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    public void testCustomerConstructorAndFields() {
        Customer customer = new Customer("John Doe", "john@example.com");
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    public void testLombokGeneratedMethods() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        assertEquals("Jane Doe", customer.getName());
    }

    @Test
    public void testBoundaryConditions() {
        Customer customer = new Customer();
        customer.setName("");
        assertEquals("", customer.getName());
    }

    @Test
    public void testNullAndInvalidValues() {
        Customer customer = new Customer();
        customer.setName(null);
        assertNull(customer.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        Customer customer1 = new Customer("John Doe", "john@example.com");
        Customer customer2 = new Customer("John Doe", "john@example.com");
        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    public void testToString() {
        Customer customer = new Customer("John Doe", "john@example.com");
        assertNotNull(customer.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        Customer customer1 = new Customer("John Doe", "john@example.com");
        Customer customer2 = new Customer("Jane Doe", "jane@example.com");
        assertNotEquals(customer1, customer2);
        assertNotEquals(customer1.hashCode(), customer2.hashCode());

        assertNotEquals(customer1, null);
        assertNotEquals(customer1, new Object());
    }

    @Test
    public void testEqualsWithDifferentName() {
        Customer customer1 = new Customer("Customer1", "customer1@example.com");
        Customer customer2 = new Customer("Customer2", "customer1@example.com");
        assertNotEquals(customer1, customer2);
    }

    @Test
    public void testEqualsWithDifferentEmail() {
        Customer customer1 = new Customer("Customer1", "customer1@example.com");
        Customer customer2 = new Customer("Customer1", "customer2@example.com");
        assertNotEquals(customer1, customer2);
    }

    @Test
    public void testEqualsWithNullFields() {
        Customer customer1 = new Customer(null, null);
        Customer customer2 = new Customer(null, null);
        assertEquals(customer1, customer2);
    }

    @Test
    public void testHashCodeConsistency() {
        Customer customer = new Customer("Customer1", "customer1@example.com");
        int initialHashCode = customer.hashCode();
        assertEquals(initialHashCode, customer.hashCode());
        assertEquals(initialHashCode, customer.hashCode());
    }
} 