/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.order;

import com.mp.be.database.entities.Order;
import com.mp.be.services.ServiceOptions;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.order.OrderDataModel;
import com.mp.be.models.order.OrderRequestModel;
import com.mp.be.models.generic.ImportRequestModel;
import com.mp.be.services.order.OrderService;
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
@RequestMapping("/api/tenant/{tenantId}/order")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping("")
    public ResponseEntity<ListResponseModel<Order>> findAndCountAll(HttpServletRequest request,
                                     @ModelAttribute OrderRequestModel requestModel,
                                     Optional<Integer> offset,
                                     Optional<Integer> limit,
                                     Optional <String> orderBy
                                     ) {
        
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<Order> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<Order> response = new ListResponseModel<Order>();
        response.rows =  pageData.getContent();
        response.count =  pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/autocomplete")
    public List<AutoComplete> autocomplete(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) Integer limit){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        List<Order> records = service.findAll(serviceOptions);
        List<AutoComplete> suggestions = new ArrayList<>();
        for (Order record: records) {
            AutoComplete suggestion = new AutoComplete();
            suggestion.id = record.id;
            suggestion.label = record.id;
            suggestions.add(suggestion);
        }
        return  suggestions;
    }

    @GetMapping("/{id}")
    public Order find(HttpServletRequest request, @PathVariable String id ){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions, id);
    }

    @PostMapping("")
    public Order create(HttpServletRequest request, @RequestBody OrderDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Order data = body.data;
        return service.create(serviceOptions, data);
    }

    @PutMapping("/{id}")
    public Order update(HttpServletRequest request, @PathVariable String id, @RequestBody OrderDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Order data = body.data;
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
                          @RequestBody ImportRequestModel<Order> body) {
        Order data = body.getData();
        ServiceOptions serviceOptions = new ServiceOptions(request);

        service.importData(serviceOptions, data, body.getImportHash());
        
        return true;
    }
}