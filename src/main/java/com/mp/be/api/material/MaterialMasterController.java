package com.mp.be.api.material;

import com.mp.be.database.entities.MaterialMaster;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.material.MaterialMasterDataModel;
import com.mp.be.models.material.MaterialMasterModel;
import com.mp.be.models.material.MaterialMasterRequestModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.material.MaterialMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "02. Material Master", description = "Master data for Raw Materials, Intermediates, Finished Items, Chemicals, Packaging, and Scrap")
@RestController
@RequestMapping("/api/tenant/{tenantId}/material")
public class MaterialMasterController {

    @Autowired
    private MaterialMasterService service;

    @Operation(summary = "Find and paginate materials with optional filters")
    @GetMapping("")
    public ResponseEntity<ListResponseModel<MaterialMasterModel>> findAndCountAll(
            HttpServletRequest request,
            @ModelAttribute MaterialMasterRequestModel requestModel,
            Optional<Integer> offset,
            Optional<Integer> limit,
            Optional<String> orderBy) {

        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<MaterialMasterModel> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<MaterialMasterModel> response = new ListResponseModel<>();
        response.rows = pageData.getContent();
        response.count = pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all active materials for the current tenant")
    @GetMapping("/all")
    public ResponseEntity<List<MaterialMasterModel>> findAll(HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.findAll(serviceOptions));
    }

    @Operation(summary = "Get material by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MaterialMasterModel> find(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        MaterialMasterModel model = service.find(serviceOptions, id);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Create a new material master record")
    @PostMapping("")
    public ResponseEntity<MaterialMasterModel> create(HttpServletRequest request, @RequestBody MaterialMasterDataModel dataModel) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.create(serviceOptions, dataModel.data));
    }

    @Operation(summary = "Update an existing material master record")
    @PutMapping("/{id}")
    public ResponseEntity<MaterialMasterModel> update(
            HttpServletRequest request,
            @PathVariable String id,
            @RequestBody MaterialMasterDataModel dataModel) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return ResponseEntity.ok(service.update(serviceOptions, id, dataModel.data));
    }

    @Operation(summary = "Delete a material master record")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        service.delete(serviceOptions, id);
        return ResponseEntity.ok().build();
    }
}
