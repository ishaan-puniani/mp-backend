package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.MeasurementUnit;
import com.mp.be.database.enumerator.RatioBasis;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Process_MixRecipeItem", description = "Ingredient formulation item in a batch mixing recipe")
public class MixRecipeItem {

    @Schema(description = "Node ID from which this raw material feeds in", example = "milk-godown")
    private String sourceNodeId;

    @Schema(description = "MaterialMaster catalog unique material code", example = "milk")
    private String ingredientCode;

    @Schema(description = "Ingredient display label", example = "Milk")
    private String ingredient;

    @Schema(description = "Proportion / Recipe ratio", example = "0.58")
    private Double ratio;

    @Schema(description = "Measurement unit for the ingredient", example = "L")
    private MeasurementUnit unit;

    @Schema(description = "Basis of ratio calculation (SHARE, PER_UNIT, PERCENTAGE, FIXED_QUANTITY)", example = "share")
    private RatioBasis ratioBasis;
}
