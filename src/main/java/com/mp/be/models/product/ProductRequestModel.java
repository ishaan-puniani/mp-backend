/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.product;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import static java.util.Optional.ofNullable;

@Schema(name = "Product_RequestModel", description = "Product Query and Filter Request Model")
public class ProductRequestModel {

    private Map<String, Object> filter;

    public ProductRequestModel(){}

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }
}