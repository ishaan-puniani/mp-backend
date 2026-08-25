/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.product;

import com.mp.be.database.entities.Product;
import com.mp.be.models.product.ProductRequestModel;
import com.mp.be.models.product.ProductModel;
import com.mp.be.services.ServiceOptions;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

public interface ProductService {

    public Page<ProductModel> findAndCountAll(ServiceOptions serviceOptions,
                                        ProductRequestModel requestModel,
                                         Optional<Integer> limit,
                                         Optional<Integer> offset,
                                         Optional <String> orderBy);
 
    public ProductModel create(ServiceOptions serviceOptions, Product data);
    
    public Product importData(ServiceOptions serviceOptions, Product data, String importHash);

    public ProductModel find(ServiceOptions serviceOptions, String id);

    public List<ProductModel> findAll(ServiceOptions serviceOptions);

    public void delete(ServiceOptions serviceOptions, String id );

    public ProductModel update(ServiceOptions serviceOptions, String id , Product data);
}
