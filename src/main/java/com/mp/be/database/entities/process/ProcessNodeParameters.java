package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Process_NodeParameters", description = "Station Operating Parameters, Feed, Capacity, and Recipes")
public class ProcessNodeParameters {

    private Object inputMaterial;
    private Object outputMaterial;
    private Object wastageMaterial;

    private Double capacity;
    private Double stock;
    private Double fillPercentage;

    private Double expectedInputFeed;
    private Double expectedOutput;
    private Double expectedWastage;

    private Double expectedInput;
    private Double expectedDispatch;
    private Double expectedInbound;

    private Map<String, String> units;
    private List<MaterialPropertyItem> materialProperties;
    private List<MixRecipeItem> mixRecipe;
    private List<SecondaryOutput> secondaryOutputs;
    private Map<String, Object> customTransform;

    public String getInputMaterialId() {
        return extractId(inputMaterial);
    }

    public String getOutputMaterialId() {
        return extractId(outputMaterial);
    }

    public String getWastageMaterialId() {
        return extractId(wastageMaterial);
    }

    private String extractId(Object obj) {
        if (obj == null) return null;
        if (obj instanceof String) return (String) obj;
        if (obj instanceof Map) {
            Object id = ((Map<?, ?>) obj).get("id");
            return id != null ? id.toString() : null;
        }
        return obj.toString();
    }
}
