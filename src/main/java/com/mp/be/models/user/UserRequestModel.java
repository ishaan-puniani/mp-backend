/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.user;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestModel {

    public Map<String, Object> filter;

    public UserRequestModel(){}

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

}