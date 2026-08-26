package com.mp.be.database.entities;

import com.mp.be.database.enumerator.ItemStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Document(collection = "mp_products")
@CompoundIndexes({
        @CompoundIndex(name = "product_tenant_code_idx", def = "{'tenant': 1, 'code': 1}", unique = true)
})
public class Product extends BaseEntity {

    public String name;
    public String code;
    public String partCode;
    public String sku;
    public String description;
    public String category;
    public String brand;
    public String barcode;
    public String hsnCode;
    public Double gstRate;

    public String baseUnit;
    public double pricing;
    public Double mrp;
    public Double costPrice;

    public Double weight;
    public String weightUnit;
    public Integer shelfLifeDays;
    public String storageTemperature;

    public Double minStockLevel;
    public Double maxStockLevel;

    private ItemStatus status = ItemStatus.ACTIVE;

    private String processConfigCode;

    @Field(targetType = FieldType.OBJECT_ID)
    private String addBy;

    private Map<String, Object> specifications;

    private List<File> image;
    private List<File> uploadedFile;
}