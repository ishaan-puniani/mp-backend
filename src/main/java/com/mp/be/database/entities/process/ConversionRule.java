package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.ConversionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Mathematical conversion rule governing throughput transformation across a directed edge")
public class ConversionRule {

    @Schema(description = "Conversion mechanism (IDENTITY, DIVIDE_BY_PROPERTY, MULTIPLY_BY_PROPERTY, DENSITY, AIR_OVERRUN, CUSTOM_FORMULA)", example = "DIVIDE_BY_PROPERTY")
    private ConversionType type;

    @Schema(description = "Property in measurementProfile applied during conversion (e.g. litersPerCarton)", example = "litersPerCarton")
    private String property;

    @Schema(description = "Optional mathematical expression when type is CUSTOM_FORMULA", example = "input * (1 + overrun/100)")
    private String formula;
}
