/// File is generated from https://studio.fabbuilder.com -
package com.mp.be.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection ="tenants")
public class Tenant extends BaseEntity {
    private String name;
    private String url;
    private String plan;
    private String planStatus;
    private String planStripeCustomerId;
    private String planUserId;

    @DBRef
    private Setting settings;
}