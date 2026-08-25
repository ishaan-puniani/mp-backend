package com.mp.be.api.product;

import com.mp.be.database.entities.Product;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.product.ProductDataModel;
import com.mp.be.models.product.ProductRequestModel;
import com.mp.be.models.generic.ImportRequestModel;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.product.ProductModel;
import com.mp.be.services.product.ProductService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        ProductModel productModel = new ProductModel();
        Page<ProductModel> page = new PageImpl<>(Collections.singletonList(productModel));

        when(productService.findAndCountAll(any(ServiceOptions.class), any(ProductRequestModel.class), any(), any(), any()))
                .thenReturn(page);
        ResponseEntity<ListResponseModel<ProductModel>> response = productController.findAndCountAll(request, new ProductRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        assertEquals(200, response.getStatusCodeValue());
        ListResponseModel<ProductModel> responseBody = response.getBody();
        assertEquals(1, responseBody.rows.size());
    }

    @Test
    void testAutocomplete() {
        when(productService.findAll(any(ServiceOptions.class))).thenReturn(Collections.singletonList(new ProductModel()));
        List<AutoComplete> response = productController.autocomplete(request, "query", 10);
        assertEquals(1, response.size());
    }

    @Test
    void testFind() {
        String productId = "1";
        ProductModel productModel = new ProductModel();

        when(productService.find(any(ServiceOptions.class), eq(productId))).thenReturn(productModel);
        ProductModel response = productController.find(request, productId);
        assertEquals(productModel, response);
    }

    @Test
    void testCreate() {
        ProductModel productModel = new ProductModel();
        ProductDataModel productDataModel = new ProductDataModel();
        productDataModel.data = new Product();

        when(productService.create(any(ServiceOptions.class), any(Product.class))).thenReturn(productModel);
        ProductModel response = productController.create(request, productDataModel);
        assertEquals(productModel, response);
    }

    @Test
    void testUpdate() {
        String productId = "1";
        ProductModel productModel = new ProductModel();
        ProductDataModel productDataModel = new ProductDataModel();
        productDataModel.data = new Product();

        when(productService.update(any(ServiceOptions.class), eq(productId), any(Product.class))).thenReturn(productModel);
        ProductModel response = productController.update(request, productId, productDataModel);
        assertEquals(productModel, response);
    }

    @Test
    void testDelete() {
        List<String> ids = List.of("1", "2");

        doNothing().when(productService).delete(any(ServiceOptions.class), any(String.class));
        productController.delete(request, ids);
    }

    @Test
    void testImportData() {
        Product product = new Product();
        ImportRequestModel<Product> importRequestModel = new ImportRequestModel<>();
        importRequestModel.setData(product);

        when(productService.importData(any(ServiceOptions.class), any(Product.class), any(String.class))).thenReturn(product);
        boolean result = productController.importData(request, importRequestModel);
        assertEquals(true, result);
    }
} 