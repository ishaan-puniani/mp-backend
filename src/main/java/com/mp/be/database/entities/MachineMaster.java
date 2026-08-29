package com.mp.be.database.entities;

import com.mp.be.database.enumerator.ItemStatus;
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
@Document(collection = "mp_machines")
@CompoundIndexes({
        @CompoundIndex(name = "machine_tenant_code_idx", def = "{'tenant': 1, 'code': 1}", unique = true)
})
public class MachineMaster extends BaseEntity {

    private String name;
    private String code;
    private String shopName;
    private String machineType;
    private Double capacity;
    private String capacityUnit;
    private ItemStatus status = ItemStatus.ACTIVE;
    private Map<String, Object> parameters;

    private List<File> image;
    private List<File> uploadedFile;
}
