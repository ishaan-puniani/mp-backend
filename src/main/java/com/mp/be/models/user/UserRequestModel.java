/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.user;

import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "User_RequestModel", description = "User Query and Filter Request Model")
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