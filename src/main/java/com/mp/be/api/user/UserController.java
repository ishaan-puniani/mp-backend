/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.user;

import com.mp.be.database.entities.User;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.user.*;
import com.mp.be.services.ServiceOptions;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenant/{tenantId}/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @GetMapping("")
    public ResponseEntity<ListResponseModel<UserModel>> findAndCountAll(HttpServletRequest request,
                                                                      @ModelAttribute UserRequestModel requestModel,
                                                                      Optional<Integer> offset,
                                                                      Optional<Integer> limit,
                                                                      Optional <String> orderBy
    ) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<UserModel> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<UserModel> response = new ListResponseModel<UserModel>();
        response.rows =  pageData.getContent();
        response.count =  pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public  UserModel find(HttpServletRequest request, @PathVariable String id) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions,id);
    }


    @PostMapping("")
    public ResponseEntity<String> create(HttpServletRequest request, @RequestBody UsersDataModel body) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        UsersModel data = body.data;

        for(String email : data.emails){
                UserModel user = new UserModel();
                user.email = email;
                user.roles = data.roles;
                service.create(serviceOptions, user);
        }


        return ResponseEntity.ok().body("User created successfully");
    }


    @PutMapping("")
    public ResponseEntity<UserModel> update(HttpServletRequest request, @RequestBody UserDataModel body) {
        ServiceOptions serviceOptions = new ServiceOptions(request);
        UserModel data = body.data;
        UserModel existingUser = service.update(serviceOptions, body.data.id, data);


        return  ResponseEntity.ok(existingUser);
    }


    @DeleteMapping("")
    public void delete(@RequestParam(name = "ids[]") List<String> ids) {
        for (String id : ids) {
            service.delete(id);
        }
    }

    @GetMapping("/autocomplete")
    public List<AutoComplete> autocomplete(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) Integer limit){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        List<User> records = service.findAll(serviceOptions);
        List<AutoComplete> suggestions = new ArrayList<>();
        for (User record: records) {
            AutoComplete suggestion = new AutoComplete();
            suggestion.id = record.id;
            suggestion.label = record.id;
            suggestions.add(suggestion);
        }
        return  suggestions;
    }
}
