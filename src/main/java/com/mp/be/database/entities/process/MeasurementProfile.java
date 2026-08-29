package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Process_MeasurementProfile", description = "Packaging hierarchy & volumetric-to-discrete conversion profile for the manufacturing plant")
public class MeasurementProfile {

    @Schema(description = "Unique identifier of the measurement profile", example = "icecream-std-profile")
    private String profileId;

    @Schema(description = "Human-readable description", example = "500ml Tub Packaging Profile")
    private String description;

    @Schema(description = "Primary bulk measurement unit", example = "L")
    private String primaryUnit;

    @Schema(description = "Secondary discrete packaging unit", example = "carton")
    private String secondaryUnit;

    @Schema(description = "Dynamic conversion ratios (e.g. litersPerCarton, cartonsPerBox, boxesPerPallet)", example = "{\"litersPerCarton\": 0.5, \"cartonsPerBox\": 12, \"boxesPerPallet\": 40}")
    private Map<String, Object> conversionProperties;
}
