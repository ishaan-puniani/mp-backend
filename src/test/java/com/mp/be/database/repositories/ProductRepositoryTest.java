package com.mp.be.database.repositories;

import com.mp.be.database.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindById() {
        Product product = new Product();
        product.setId("prod-1");
        product.setName("SS DRIP TRAY FOR S.DLX GR-I");
        product.setCode("FBDR-SB005-00");
        product.setPricing(760.0);

        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(product));

        Product saved = productRepository.save(product);
        Product found = productRepository.findById("prod-1").orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("SS DRIP TRAY FOR S.DLX GR-I");
        assertThat(found.getCode()).isEqualTo("FBDR-SB005-00");
    }
}