/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.customer;

import com.mp.be.database.entities.Customer;
import com.mp.be.models.customer.CustomerRequestModel;
import com.mp.be.models.customer.CustomerModel;
import com.mp.be.services.ServiceOptions;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

public interface CustomerService {

    public Page<Customer> findAndCountAll(ServiceOptions serviceOptions,
                                        CustomerRequestModel requestModel,
                                         Optional<Integer> limit,
                                         Optional<Integer> offset,
                                         Optional <String> orderBy);
 
    public Customer create(ServiceOptions serviceOptions, Customer data);
    
    public Customer importData(ServiceOptions serviceOptions, Customer data, String importHash);

    public Customer find(ServiceOptions serviceOptions, String id);

    public List<Customer> findAll(ServiceOptions serviceOptions);

    public void delete(ServiceOptions serviceOptions, String id );

    public Customer update(ServiceOptions serviceOptions, String id , Customer data);
}
