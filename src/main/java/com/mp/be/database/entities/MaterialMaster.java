package com.mp.be.database.entities;

import com.mp.be.database.enumerator.ItemStatus;
import com.mp.be.database.enumerator.MaterialType;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Document(collection = "mp_materials")
@CompoundIndexes({
        @CompoundIndex(name = "material_tenant_code_idx", def = "{'tenant': 1, 'code': 1}", unique = true)
})
public class MaterialMaster extends BaseEntity {

    private String name;
    private String code;
    private MaterialType type;
    private String baseUnit;
    private Double density;
    private Double standardCost;
    private String description;
    private ItemStatus status = ItemStatus.ACTIVE;
    private Map<String, Object> attributes;

    private List<File> image;
    private List<File> uploadedFile;
}
