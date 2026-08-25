/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.tenant;

import com.mp.be.models.tenant.TenantDataModel;
import com.mp.be.database.entities.Tenant;
import com.mp.be.database.entities.User;
import com.mp.be.services.tenant.TenantService;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.tenant.TenantRequestModel;
import com.mp.be.services.ServiceOptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    private TenantService service;

    @PostMapping("")
    public ResponseEntity<Tenant> createTenant(@RequestBody TenantDataModel body, HttpServletRequest request) {
            User currentUser = (User) request.getAttribute("currentUser");
            Tenant data = body.data;
            Tenant tenant = service.create(data, currentUser);
            return ResponseEntity.ok(tenant);
    }

    @GetMapping("")
    public ResponseEntity<ListResponseModel<Tenant>> list(HttpServletRequest request,
                                                          @ModelAttribute TenantRequestModel requestModel,
                                                          Optional<Integer> offset,
                                                          Optional<Integer> limit,
                                                          Optional <String> orderBy) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<Tenant> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<Tenant> response = new ListResponseModel<Tenant>();
        response.rows =  pageData.getContent();
        response.count =  pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Tenant find(@PathVariable String id ){
        return service.find(id);
    }

    @PostMapping("/invitation/{token}/accept")
    public ResponseEntity<Tenant> acceptInvitation(@PathVariable String token,
                                                 @RequestBody Map<String, Boolean> payload,
                                                 HttpServletRequest request) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        User currentUser = (User) request.getAttribute("currentUser");
        boolean forceAcceptOtherEmail = payload.getOrDefault("forceAcceptOtherEmail", false);
        Tenant tenant = service.acceptInvitation(token, currentUser,serviceOptions );
          return ResponseEntity.ok(tenant);
    }
}