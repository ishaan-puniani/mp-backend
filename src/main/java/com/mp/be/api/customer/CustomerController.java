/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.customer;

import com.mp.be.database.entities.Customer;
import com.mp.be.services.ServiceOptions;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.customer.CustomerDataModel;
import com.mp.be.models.customer.CustomerRequestModel;
import com.mp.be.models.generic.ImportRequestModel;
import com.mp.be.services.customer.CustomerService;
import com.mp.be.models.AutoComplete;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/api/tenant/{tenantId}/customer")
public class CustomerController {
    @Autowired
    private CustomerService service;

    @GetMapping("")
    public ResponseEntity<ListResponseModel<Customer>> findAndCountAll(HttpServletRequest request,
                                     @ModelAttribute CustomerRequestModel requestModel,
                                     Optional<Integer> offset,
                                     Optional<Integer> limit,
                                     Optional <String> orderBy
                                     ) {
        
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<Customer> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<Customer> response = new ListResponseModel<Customer>();
        response.rows =  pageData.getContent();
        response.count =  pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/autocomplete")
    public List<AutoComplete> autocomplete(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) Integer limit){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        List<Customer> records = service.findAll(serviceOptions);
        List<AutoComplete> suggestions = new ArrayList<>();
        for (Customer record: records) {
            AutoComplete suggestion = new AutoComplete();
            suggestion.id = record.id;
            suggestion.label = record.id;
            suggestions.add(suggestion);
        }
        return  suggestions;
    }

    @GetMapping("/{id}")
    public Customer find(HttpServletRequest request, @PathVariable String id ){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions, id);
    }

    @PostMapping("")
    public Customer create(HttpServletRequest request, @RequestBody CustomerDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Customer data = body.data;
        return service.create(serviceOptions, data);
    }

    @PutMapping("/{id}")
    public Customer update(HttpServletRequest request, @PathVariable String id, @RequestBody CustomerDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Customer data = body.data;
        return service.update(serviceOptions, id, data);
    }

    @DeleteMapping("")
    public void delete(HttpServletRequest request, @RequestParam(name = "ids[]") List<String> ids){
        ServiceOptions serviceOptions = new ServiceOptions(request);

        for (String id : ids) {
            service.delete(serviceOptions, id);
        }
    }

    @PostMapping("/import")
    public boolean importData(HttpServletRequest request, 
                          @RequestBody ImportRequestModel<Customer> body) {
        Customer data = body.getData();
        ServiceOptions serviceOptions = new ServiceOptions(request);

        service.importData(serviceOptions, data, body.getImportHash());
        
        return true;
    }
}