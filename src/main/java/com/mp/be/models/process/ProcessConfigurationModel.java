package com.mp.be.models.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.entities.process.MeasurementProfile;
import com.mp.be.database.entities.process.ProcessEdge;
import com.mp.be.database.entities.process.ProcessNode;
import com.mp.be.database.enumerator.ProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Process_ConfigurationModel", description = "Manufacturing Process Workflow Configuration Model")
public class ProcessConfigurationModel {

    @Schema(description = "Primary unique identifier", example = "64b0f9c2a1e45c001f3b8901")
    private String id;

    @Schema(description = "Process display name", example = "Ice Cream Manufacturing Plant")
    private String name;

    @Schema(description = "Process Code / SOP Reference", example = "PROC-IC-001")
    private String code;

    @Schema(description = "Detailed plant and flow description", example = "Automated industrial ice cream manufacturing workflow")
    private String description;

    @Schema(description = "Associated Finished Product Code", example = "PROD-ICECREAM-01")
    private String productCode;

    @Schema(description = "Configuration version", example = "1")
    private Integer version;

    @Schema(description = "Process status", example = "ACTIVE")
    private ProcessStatus status;

    @Schema(description = "Plant volumetric-to-discrete conversion & packaging profile")
    private MeasurementProfile measurementProfile;

    @Schema(description = "List of all manufacturing workstations, machines, and storage godowns")
    private List<ProcessNode> nodes;

    @Schema(description = "List of all directed material transfer pipelines and connections")
    private List<ProcessEdge> edges;

    @Schema(description = "Extensible metadata attributes", example = "{\"plantCode\": \"ICECREAM-DELHI-01\"}")
    private Map<String, Object> metadata;

    @Schema(description = "Creation timestamp")
    private Date createdAt;

    @Schema(description = "Last update timestamp")
    private Date updatedAt;
}
