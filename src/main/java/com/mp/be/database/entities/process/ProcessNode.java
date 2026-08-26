package com.mp.be.database.entities.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mp.be.database.enumerator.NodeKind;
import com.mp.be.database.enumerator.NodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Workstation / Machine station or godown in the manufacturing plant DAG")
public class ProcessNode {

    @Schema(description = "Node station unique identifier", example = "mixing")
    private String id;

    @Schema(description = "Display title on the process canvas", example = "Mixing")
    private String label;

    @Schema(description = "Standard operating procedure / Station code", example = "PROC-MIX-01")
    private String subtitle;

    @Schema(description = "Node classification (machine, godown, qc, manual)", example = "machine")
    private NodeKind nodeKind;

    @Schema(description = "Grid layout column on visual canvas", example = "1")
    private Integer column;

    @Schema(description = "Grid layout row on visual canvas", example = "2")
    private Integer row;

    @Schema(description = "Current operational status", example = "running")
    private NodeStatus status;

    @Schema(description = "Machine Master catalog linkage unique code", example = "MACH-MIX-01")
    private String machineCode;

    @Schema(description = "Operating parameters (capacity, feed rate, mix recipes, throughput)")
    private ProcessNodeParameters parameters;

    @Schema(description = "Allocated human workforce & operators")
    private List<ProcessLabourRequirement> processLabour;

    @Schema(description = "Consumables (cartons, lids, CIP chemicals) consumed at this station")
    private List<ProcessConsumableRequirement> consumables;

    @Schema(description = "Direct utilities running cost rules (Power, Steam, Refrigeration)")
    private List<ProcessCostRule> processCosts;
}
