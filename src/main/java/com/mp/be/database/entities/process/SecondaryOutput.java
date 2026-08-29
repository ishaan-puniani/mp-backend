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
@Schema(name = "Process_SecondaryOutput", description = "By-product or Secondary Material Stream Specification")
public class SecondaryOutput {
    private String key;
    private String label;
    private String unit;
    private Double perPrimaryUnit;
    private String basis = "stock";
}
