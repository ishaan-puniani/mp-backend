/// File is generated from https://studio.fabbuilder.com -
package com.mp.be.database.entities;

import java.util.List;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection ="tenantUser")
public class TenantUser {

        @Id
        private String id;

        private List<String> roles;

        @Field(targetType = FieldType.OBJECT_ID)
        private String tenant;

        private String status;
        private String invitationToken;
        @LastModifiedDate
        private Date updatedAt;
        @CreatedDate
        private Date createdAt;

}
