package com.mp.be.api.machine;

import com.mp.be.database.entities.MachineMaster;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.machine.MachineMasterDataModel;
import com.mp.be.models.machine.MachineMasterModel;
import com.mp.be.models.machine.MachineMasterRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.machine.MachineMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "03. Machine & Shop Master", description = "Master data for factory workcenters, production lines, machines, and shop floors")
@RestController
@RequestMapping("/api/tenant/{tenantId}/machine")
public class MachineMasterController {

    @Autowired
    private MachineMasterService service;

    @Operation(summary = "Find and paginate machines with optional filters")
    @GetMapping("")
    public ResponseEntity<ListResponseModel<MachineMasterModel>> findAndCountAll(
            HttpServletRequest request,
            @ModelAttribute MachineMasterRequestModel requestModel,
            Optional<Integer> offset,
            Optional<Integer> limit,
            Optional<String> orderBy) {

        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<MachineMasterModel> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<MachineMasterModel> response = new ListResponseModel<>();
        response.rows = pageData.getContent();
        response.count = pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all active machines for the current tenant")
    @GetMapping("/all")
    public ResponseEntity<List<MachineMasterModel>> findAll(HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.findAll(serviceOptions));
    }

    @Operation(summary = "Get machine by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MachineMasterModel> find(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        MachineMasterModel model = service.find(serviceOptions, id);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Create a new machine / shop record")
    @PostMapping("")
    public ResponseEntity<MachineMasterModel> create(HttpServletRequest request, @RequestBody MachineMasterDataModel dataModel) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.create(serviceOptions, dataModel.data));
    }

    @Operation(summary = "Update an existing machine / shop record")
    @PutMapping("/{id}")
    public ResponseEntity<MachineMasterModel> update(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody MachineMasterDataModel dataModel) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.update(serviceOptions, id, dataModel.data));
    }

    @Operation(summary = "Delete a machine / shop record")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        service.delete(serviceOptions, id);
        return ResponseEntity.ok().build();
    }
}
