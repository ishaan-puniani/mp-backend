package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Process_MaterialPropertyItem", description = "Dynamic Physical Property Key-Value Specification")
public class MaterialPropertyItem {
    private String key;
    private String label;
    private Object value;
    private String unit;
}
