/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.auditLog;

import com.mp.be.database.entities.AuditLog;
import com.mp.be.models.auditLog.AuditLogRequestModel;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.auditLog.AuditLogService;
import com.mp.be.services.ServiceOptions;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tenant/{tenantId}/audit-log")
public class AuditLogController {
    @Autowired
    private AuditLogService service;


    @GetMapping("")
    public ResponseEntity<Object> findAndCountAll(HttpServletRequest request,
                                                  @ModelAttribute AuditLogRequestModel requestModel,
                                                  Optional<Integer> offset,
                                                  Optional<Integer> limit,
                                                  Optional<String> orderBy)
    {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<AuditLog> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<AuditLog> response = new ListResponseModel<AuditLog>();
        response.rows =  pageData.getContent();
        response.count =  pageData.getTotalElements();
        return ResponseEntity.ok(response);


    }

    @GetMapping("/{id}")
    public AuditLog find(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions, id);
    }

    @PostMapping("/")
    public AuditLog create(HttpServletRequest request, @RequestBody AuditLog data) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.create(serviceOptions, data);
    }

    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        service.delete(serviceOptions, id);
    }

}