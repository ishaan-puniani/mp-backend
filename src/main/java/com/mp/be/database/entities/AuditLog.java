/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "mp_auditlogs")
public class AuditLog extends BaseEntity {
    public String entityName;
    public String entityId;
    public String action;
    public String tenantId;
    public String createdById;
    public String createdByEmail;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public Date timestamp;
    public Map<String, Object> values;
}