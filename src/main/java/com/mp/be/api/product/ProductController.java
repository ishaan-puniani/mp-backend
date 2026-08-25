/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.api.product;

import com.mp.be.database.entities.Product;
import com.mp.be.models.product.ProductModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.models.product.ProductDataModel;
import com.mp.be.models.product.ProductRequestModel;
import com.mp.be.models.generic.ImportRequestModel;
import com.mp.be.services.product.ProductService;
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
@RequestMapping("/api/tenant/{tenantId}/product")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("")
    public ResponseEntity<ListResponseModel<ProductModel>> findAndCountAll(HttpServletRequest request,
                                     @ModelAttribute ProductRequestModel requestModel,
                                     Optional<Integer> offset,
                                     Optional<Integer> limit,
                                     Optional <String> orderBy
                                     ) {
        
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Page<ProductModel> pageData = service.findAndCountAll(serviceOptions, requestModel, limit, offset, orderBy);

        ListResponseModel<ProductModel> response = new ListResponseModel<ProductModel>();
        response.rows =  pageData.getContent();
        response.count =  pageData.getTotalElements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/autocomplete")
    public List<AutoComplete> autocomplete(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) Integer limit){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        List<ProductModel> records = service.findAll(serviceOptions);
        List<AutoComplete> suggestions = new ArrayList<>();
        for (ProductModel record: records) {
            AutoComplete suggestion = new AutoComplete();
            suggestion.id = record.getId();
            suggestion.label = record.getId();
            suggestions.add(suggestion);
        }
        return  suggestions;
    }

    @GetMapping("/{id}")
    public ProductModel find(HttpServletRequest request, @PathVariable String id ){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        return service.find(serviceOptions, id);
    }

    @PostMapping("")
    public ProductModel create(HttpServletRequest request, @RequestBody ProductDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Product data = body.data;
        return service.create(serviceOptions, data);
    }

    @PutMapping("/{id}")
    public ProductModel update(HttpServletRequest request, @PathVariable String id, @RequestBody ProductDataModel body){
        ServiceOptions serviceOptions = new ServiceOptions(request);
        Product data = body.data;
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
                          @RequestBody ImportRequestModel<Product> body) {
        Product data = body.getData();
        ServiceOptions serviceOptions = new ServiceOptions(request);

        service.importData(serviceOptions, data, body.getImportHash());
        
        return true;
    }
}