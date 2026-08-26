package com.mp.be.api.process;

import com.mp.be.database.entities.process.ProcessConfiguration;
import com.mp.be.database.enumerator.ProcessStatus;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.process.ProcessConfigurationDataModel;
import com.mp.be.models.process.ProcessConfigurationModel;
import com.mp.be.models.process.ProcessConfigurationRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.process.ProcessConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "01. Process Configuration", description = "Plant workflow topology, DAG nodes, transformation edges, and ingredient mix recipes")
@RestController
@RequestMapping("/api/tenant/{tenantId}/process-configuration")
public class ProcessConfigurationController {

    @Autowired
    private ProcessConfigurationService service;

    @Operation(summary = "Find and paginate process configurations with optional filters")
    @GetMapping("")
    public ResponseEntity<ListResponseModel<ProcessConfigurationModel>> findAndCountAll(
            HttpServletRequest request,
            @ModelAttribute ProcessConfigurationRequestModel requestModel,
            Optional<Integer> offset,
            Optional<Integer> limit,
            Optional<String> orderBy) {

        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<ProcessConfigurationModel> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<ProcessConfigurationModel> response = new ListResponseModel<>();
        response.rows = pageData.getContent();
        response.count = pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all active process configurations for the current tenant")
    @GetMapping("/all")
    public ResponseEntity<List<ProcessConfigurationModel>> findAll(HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.findAll(serviceOptions));
    }

    @Operation(summary = "Get process configuration by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProcessConfigurationModel> find(
            HttpServletRequest request,
            @Parameter(description = "Process Configuration ID") @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        ProcessConfigurationModel model = service.find(serviceOptions, id);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Seed complete Ice Cream Manufacturing process configuration with exact nodes, recipes, and India metadata")
    @PostMapping("/seed/icecream")
    public ResponseEntity<ProcessConfigurationModel> seedIceCream(HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.seedIceCreamProcess(serviceOptions));
    }

    @Operation(summary = "Create a new process configuration graph (nodes, edges, recipes)")
    @PostMapping("")
    public ResponseEntity<ProcessConfigurationModel> create(
            HttpServletRequest request,
            @RequestBody ProcessConfigurationDataModel dataModel) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.create(serviceOptions, dataModel.data));
    }

    @Operation(summary = "Update an existing process configuration")
    @PutMapping("/{id}")
    public ResponseEntity<ProcessConfigurationModel> update(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody ProcessConfigurationDataModel dataModel) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.update(serviceOptions, id, dataModel.data));
    }

    @Operation(summary = "Update status (e.g. DRAFT, ACTIVE, ARCHIVED) of a process configuration")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProcessConfigurationModel> updateStatus(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestParam ProcessStatus status) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.updateStatus(serviceOptions, id, status));
    }

    @Operation(summary = "Delete a process configuration")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        service.delete(serviceOptions, id);
        return ResponseEntity.ok().build();
    }
}
