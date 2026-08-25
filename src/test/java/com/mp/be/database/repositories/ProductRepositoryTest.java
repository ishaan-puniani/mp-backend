package com.mp.be.database.repositories;

import com.mp.be.database.entities.Product;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindById() {
        Product product = new Product("Product1", 100.0, 10, null, null, true, null, null, ObjectId.get().toString(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        productRepository.save(product);

        Product found = productRepository.findById(product.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Product1");
    }

    @Test
    void testDelete() {
        Product product = new Product("Product1", 100.0, 10, null, null, true, null, null, ObjectId.get().toString(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        productRepository.save(product);
        productRepository.delete(product);

        Product found = productRepository.findById(product.getId()).orElse(null);
        assertThat(found).isNull();
    }
}