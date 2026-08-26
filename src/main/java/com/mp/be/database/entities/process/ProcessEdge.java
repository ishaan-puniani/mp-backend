package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.EdgeRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Material flow connector / Directed edge connecting two manufacturing stations")
public class ProcessEdge {

    @Schema(description = "Edge connection identifier", example = "e-milk-mixing")
    private String id;

    @Schema(description = "Origin node station identifier", example = "milk-godown")
    private String source;

    @Schema(description = "Destination node station identifier", example = "mixing")
    private String target;

    @Schema(description = "Batch formulation mix ratio", example = "0.58")
    private Double mixRatio;

    @Schema(description = "Physical material transferring across this edge (String code or Material Object)", example = "raw-milk")
    private Object material;

    @Schema(description = "MaterialMaster catalog linkage unique code", example = "milk")
    private String materialCode;

    @Schema(description = "Throughput conversion rule applied during material transfer")
    private ConversionRule conversion;

    @Schema(description = "Edge functional classification (main, primary, ingredient, wastage, scrap, garbage)", example = "main")
    private EdgeRole role;

    public String getMaterialId() {
        if (material == null) return materialCode;
        if (material instanceof String) return (String) material;
        if (material instanceof Map) {
            Object id = ((Map<?, ?>) material).get("id");
            return id != null ? id.toString() : null;
        }
        return material.toString();
    }
}
