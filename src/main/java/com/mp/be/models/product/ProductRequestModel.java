/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.product;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

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