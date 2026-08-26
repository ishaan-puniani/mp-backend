package com.mp.be.api.user;

import com.mp.be.database.entities.User;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.user.*;
import com.mp.be.services.ServiceOptions;
import com.mp.be.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "07. User Management", description = "System users, permissions, and role management")
@RestController
@RequestMapping("/api/tenant/{tenantId}/user")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Find and paginate users with optional filters")
    @GetMapping("")
    public ResponseEntity<ListResponseModel<UserModel>> findAndCountAll(
            HttpServletRequest request,
            @ModelAttribute UserRequestModel requestModel,
            Optional<Integer> offset,
            Optional<Integer> limit,
            Optional<String> orderBy) {

        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<UserModel> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<UserModel> response = new ListResponseModel<>();
        response.rows = pageData.getContent();
        response.count = pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user details by ID")
    @GetMapping("/{id}")
    public UserModel find(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions, id);
    }

    @Operation(summary = "Invite or create users for the tenant")
    @PostMapping("")
    public ResponseEntity<String> create(HttpServletRequest request, @RequestBody UsersDataModel body) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        UsersModel data = body.data;

        for (String email : data.emails) {
            UserModel user = new UserModel();
            user.email = email;
            user.roles = data.roles;
            service.create(serviceOptions, user);
        }

        return ResponseEntity.ok().body("User created successfully");
    }

    @Operation(summary = "Update user details and roles")
    @PutMapping("")
    public ResponseEntity<UserModel> update(HttpServletRequest request, @RequestBody UserDataModel body) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        UserModel data = body.data;
        UserModel existingUser = service.update(serviceOptions, body.data.id, data);
        return ResponseEntity.ok(existingUser);
    }

    @Operation(summary = "Delete users by ID array")
    @DeleteMapping("")
    public void delete(@RequestParam(name = "ids[]") List<String> ids) {
        for (String id : ids) {
            service.delete(id);
        }
    }

    @Operation(summary = "Autocomplete suggestions for users")
    @GetMapping("/autocomplete")
    public List<AutoComplete> autocomplete(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) Integer limit) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        List<User> records = service.findAll(serviceOptions);
        List<AutoComplete> suggestions = new ArrayList<>();
        for (User record : records) {
            AutoComplete suggestion = new AutoComplete();
            suggestion.id = record.id;
            suggestion.label = record.getEmail() != null ? record.getEmail() : record.id;
            suggestions.add(suggestion);
        }
        return suggestions;
    }
}
