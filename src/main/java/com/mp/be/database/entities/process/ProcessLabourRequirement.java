package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Process_LabourRequirement", description = "Workforce and operator requirement specifications for a station")
public class ProcessLabourRequirement {

    @Schema(description = "Labour role identifier", example = "mix-op")
    private String id;

    @Schema(description = "Job title / Designation", example = "Batch mix operator")
    private String title;

    @Schema(description = "Monthly base wage / compensation", example = "22000.00")
    private Double monthlyWage;

    @Schema(description = "Currency for payroll calculation", example = "INR")
    private Currency currency;

    @Schema(description = "Monthly throughput capacity handled by this role", example = "9000.0")
    private Double monthlyCapacityUnits;

    @Schema(description = "Role description & operational duties", example = "Loads sugar syrup, stabilizer; monitors blend viscosity.")
    private String description;
}
