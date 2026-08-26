package com.mp.be.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;

@MappedSuperclass
@Document
@Setter
@Getter
public class BaseEntity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Field(targetType = FieldType.OBJECT_ID)
    public String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Field(targetType = FieldType.OBJECT_ID)
    public String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Field(targetType = FieldType.OBJECT_ID)
    public String tenant;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreatedDate
    private Date createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @LastModifiedDate
    private Date updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String importHash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (updatedBy != null ? !updatedBy.equals(that.updatedBy) : that.updatedBy != null) return false;
        if (tenant != null ? !tenant.equals(that.tenant) : that.tenant != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
        return importHash != null ? importHash.equals(that.importHash) : that.importHash == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (importHash != null ? importHash.hashCode() : 0);
        return result;
    }
}
