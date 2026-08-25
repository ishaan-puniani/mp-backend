package com.mp.be.database.entities;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import com.mp.be.database.enumerator.Categories;
import com.mp.be.database.enumerator.Status;

public class ProductTest {

    @Test
    public void testProductCreation() {
        Product product = new Product("Product1", 100.0, 10, null, null, true, null, null, "user123", Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        assertNotNull(product);
        assertEquals("Product1", product.getName());
    }

    @Test
    public void testProductConstructorAndFields() {
        Date now = new Date();
        LocalDateTime later = LocalDateTime.now();
        List<File> files = Collections.emptyList();
        List<String> approvers = List.of("approver1", "approver2");
        List<Categories> categories = List.of(Categories.shirt);
        Product product = new Product("Product1", 99.99, 10, now, later, true, Status.publish, categories, "user123", files, files, approvers);
        assertEquals("Product1", product.getName());
        assertEquals(99.99, product.getPricing());
        assertEquals(10, product.getAvailableStock());
        assertEquals(now, product.getAvailableFrom());
        assertEquals(later, product.getAvailableUpto());
        assertTrue(product.getIsActive());
        assertEquals(Status.publish, product.getStatus());
        assertEquals(categories, product.getCategories());
        assertEquals("user123", product.getAddBy());
        assertEquals(files, product.getImage());
        assertEquals(files, product.getUploadedFile());
        assertEquals(approvers, product.getApprovers());
    }

    @Test
    public void testLombokGeneratedMethods() {
        Product product = new Product();
        product.setName("Product2");
        assertEquals("Product2", product.getName());
    }

    @Test
    public void testBoundaryConditions() {
        Product product = new Product();
        product.setPricing(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, product.getPricing());
        product.setAvailableStock(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, product.getAvailableStock());
    }

    @Test
    public void testNullAndInvalidValues() {
        Product product = new Product();
        product.setName(null);
        assertNull(product.getName());
        product.setPricing(-1);
        assertEquals(-1, product.getPricing());
    }

    @Test
    public void testEqualsAndHashCode() {
        Product product1 = new Product("Product1", 99.99, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user123", Collections.emptyList(), Collections.emptyList(), List.of("approver1"));
        Product product2 = new Product("Product1", 99.99, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user123", Collections.emptyList(), Collections.emptyList(), List.of("approver1"));
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    public void testToString() {
        Product product = new Product("Product1", 99.99, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user123", Collections.emptyList(), Collections.emptyList(), List.of("approver1"));
        assertNotNull(product.toString());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentObjects() {
        Product product1 = new Product("Product1", 99.99, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user123", Collections.emptyList(), Collections.emptyList(), List.of("approver1"));
        Product product2 = new Product("Product2", 99.99, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user123", Collections.emptyList(), Collections.emptyList(), List.of("approver1"));
        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());

        assertNotEquals(product1, null);
        assertNotEquals(product1, new Object());
    }

    @Test
    public void testEqualsWithDifferentName() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product2", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentPricing() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 200.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentAvailableStock() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 20, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentAvailableFrom() {
        Date now = new Date();
        Product product1 = new Product("Product1", 100.0, 10, now, LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(now.getTime() + 1000), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentAvailableUpto() {
        LocalDateTime now = LocalDateTime.now();
        Product product1 = new Product("Product1", 100.0, 10, new Date(), now, true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), now.plusDays(1), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentIspublish() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), false, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentStatus() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.draft, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentCategories() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.dress), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentAddBy() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user2", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentImage() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(new File()), List.of(), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentUploadedFile() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(new File()), List.of());
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithDifferentApprovers() {
        Product product1 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of("approver1"));
        Product product2 = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithNullFields() {
        Product product1 = new Product(null, 0.0, null, null, null, null, null, null, null, null, null, null);
        Product product2 = new Product(null, 0.0, null, null, null, null, null, null, null, null, null, null);
        assertEquals(product1, product2);
    }

    @Test
    public void testHashCodeConsistency() {
        Product product = new Product("Product1", 100.0, 10, new Date(), LocalDateTime.now(), true, Status.publish, List.of(Categories.shirt), "user1", List.of(), List.of(), List.of());
        int initialHashCode = product.hashCode();
        assertEquals(initialHashCode, product.hashCode());
        assertEquals(initialHashCode, product.hashCode());
    }
} 