package com.mp.be.api.auditLog;

import com.mp.be.database.entities.AuditLog;
import com.mp.be.models.auditLog.AuditLogRequestModel;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.auditLog.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "09. Audit Logs", description = "Compliance audit trail and system event logs")
@RestController
@RequestMapping("/api/tenant/{tenantId}/audit-log")
public class AuditLogController {

    @Autowired
    private AuditLogService service;

    @Operation(summary = "Find and paginate audit log events with optional filters")
    @GetMapping("")
    public ResponseEntity<Object> findAndCountAll(
            HttpServletRequest request,
            @ModelAttribute AuditLogRequestModel requestModel,
            Optional<Integer> offset,
            Optional<Integer> limit,
            Optional<String> orderBy) {

        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<AuditLog> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<AuditLog> response = new ListResponseModel<>();
        response.rows = pageData.getContent();
        response.count = pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get audit log entry by ID")
    @GetMapping("/{id}")
    public AuditLog find(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions, id);
    }

    @Operation(summary = "Create an audit log entry")
    @PostMapping("")
    public AuditLog create(HttpServletRequest request, @RequestBody AuditLog data) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.create(serviceOptions, data);
    }

    @Operation(summary = "Delete an audit log entry")
    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        service.delete(serviceOptions, id);
    }
}