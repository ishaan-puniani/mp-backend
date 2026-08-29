/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.auditLog;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(name = "AuditLog_RequestModel", description = "Audit Trail Query and Filter Request Model")
public class AuditLogRequestModel{
	
 public Map<String, Object> filter;

    public AuditLogRequestModel(){}

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

}