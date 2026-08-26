package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Packaging or CIP consumable item utilized at a process station")
public class ProcessConsumableRequirement {

    @Schema(description = "Consumable identifier", example = "tub-500")
    private String id;

    @Schema(description = "Label / Name of consumable", example = "500ml IML carton tub")
    private String label;

    @Schema(description = "Technical grade / Material specification", example = "Food-grade PE lined")
    private String spec;

    @Schema(description = "Consumption quantity per basis cycle", example = "1.0")
    private Double consumptionRate;

    @Schema(description = "Measurement unit of consumption", example = "pcs")
    private MeasurementUnit unit;

    @Schema(description = "Basis of consumption ('carton', 'batch', 'hr')", example = "carton")
    private String basis;
}
