package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.entities.BaseEntity;
import com.mp.be.database.enumerator.ProcessStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "mp_process_configurations")
@CompoundIndexes({
        @CompoundIndex(name = "process_tenant_code_version_idx", def = "{'tenant': 1, 'code': 1, 'version': 1}", unique = true, sparse = true)
})
public class ProcessConfiguration extends BaseEntity {

    private String name;
    private String code;
    private String description;

    private String productCode;

    private Integer version = 1;
    private ProcessStatus status = ProcessStatus.DRAFT;

    private MeasurementProfile measurementProfile;
    private List<ProcessNode> nodes = new ArrayList<>();
    private List<ProcessEdge> edges = new ArrayList<>();

    private Map<String, Object> metadata;
}
