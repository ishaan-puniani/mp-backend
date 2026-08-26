package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.Currency;
import com.mp.be.database.enumerator.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Utility, power, or operational running cost rate associated with a process node")
public class ProcessCostRule {

    @Schema(description = "Cost identifier", example = "mix-pwr")
    private String id;

    @Schema(description = "Label of the cost component", example = "High-shear mixing power")
    private String label;

    @Schema(description = "Cost rate per unit of throughput", example = "1.10")
    private Double ratePerUnit;

    @Schema(description = "Currency ISO code for the cost rule", example = "INR")
    private Currency currency;

    @Schema(description = "Unit of measurement basis for the rate", example = "L")
    private MeasurementUnit unit;
}
