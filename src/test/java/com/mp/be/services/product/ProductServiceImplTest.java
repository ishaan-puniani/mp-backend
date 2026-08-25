package com.mp.be.services.product;

import com.mp.be.database.entities.Product;
import com.mp.be.database.entities.User;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.FileRepository;
import com.mp.be.database.repositories.ProductRepository;
import com.mp.be.models.product.ProductModel;
import com.mp.be.models.product.ProductRequestModel;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private ServiceOptions serviceOptions;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        // Arrange
        when(serviceOptions.getCurrentUser()).thenReturn(new User("test@example.com"));
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");
        ProductRequestModel requestModel = new ProductRequestModel();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Product> products = Collections.emptyList();
        Page<Product> productPage = new PageImpl<>(products, pageRequest, 0);

        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(products);
        when(mongoTemplate.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        // Act
        Page<ProductModel> result = productService.findAndCountAll(serviceOptions, requestModel, Optional.of(10), Optional.of(0), Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Product.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Product.class));
    }

    @Test
    void testFindAll() {
        // Arrange
        when(serviceOptions.getCurrentUser()).thenReturn(new User("test@example.com"));
        List<Product> products = Collections.emptyList();
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<ProductModel> result = productService.findAll(serviceOptions);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFind() {
        // Arrange
        when(serviceOptions.getCurrentUser()).thenReturn(new User("test@example.com"));
        String productId = "123";
        Product product = new Product();
        product.setApprovers(Collections.emptyList());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ProductModel result = productService.find(serviceOptions, productId);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreate() {
        // Arrange
        when(serviceOptions.getCurrentUser()).thenReturn(new User("test@example.com"));
        Product product = new Product();
        product.setCategories(Collections.emptyList());
        product.setImage(Collections.emptyList());
        product.setUploadedFile(Collections.emptyList());
        product.setApprovers(Collections.emptyList());
        when(productRepository.save(product)).thenReturn(product);

        // Act
        ProductModel result = productService.create(serviceOptions, product);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        // Arrange
        when(serviceOptions.getCurrentUser()).thenReturn(new User("test@example.com"));
        String productId = "123";
        Product product = new Product();
        product.setCategories(Collections.emptyList());
        product.setImage(Collections.emptyList());
        product.setUploadedFile(Collections.emptyList());
        product.setApprovers(Collections.emptyList());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        // Act
        ProductModel result = productService.update(serviceOptions, productId, product);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testDelete() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        String productId = "123";
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.delete(serviceOptions, productId);

        // Assert
        verify(productRepository, times(1)).delete(product);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testImportData() {
        // Arrange
        ServiceOptions serviceOptions = mock(ServiceOptions.class);
        Product product = new Product();
        String importHash = "uniqueHash";
        when(productRepository.countImportHash(importHash)).thenReturn(0L);
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.importData(serviceOptions, product, importHash);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testFindAndCountAllWithBoundaryValues() {
        // Arrange
        when(serviceOptions.getCurrentUser()).thenReturn(new User("test@example.com"));
        when(serviceOptions.getCurrentTenantId()).thenReturn("tenantId");
        ProductRequestModel requestModel = new ProductRequestModel();
        PageRequest pageRequest = PageRequest.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
        List<Product> products = Collections.emptyList();
        Page<Product> productPage = new PageImpl<>(products, pageRequest, 0);

        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(products);
        when(mongoTemplate.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        // Act
        Page<ProductModel> result = productService.findAndCountAll(serviceOptions, requestModel, Optional.of(Integer.MAX_VALUE), Optional.of(Integer.MAX_VALUE), Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Product.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Product.class));
    }

    @Test
    void testCreateWithNullProduct() {
        // Arrange
        Product product = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.create(serviceOptions, product);
        });
    }
} 