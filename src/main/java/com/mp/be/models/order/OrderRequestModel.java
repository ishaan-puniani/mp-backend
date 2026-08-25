/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.order;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class OrderRequestModel {

    private Map<String, Object> filter;

    public OrderRequestModel(){}

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }
}