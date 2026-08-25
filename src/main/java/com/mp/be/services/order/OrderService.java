/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.order;

import com.mp.be.database.entities.Order;
import com.mp.be.models.order.OrderRequestModel;
import com.mp.be.models.order.OrderModel;
import com.mp.be.services.ServiceOptions;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

public interface OrderService {

    public Page<Order> findAndCountAll(ServiceOptions serviceOptions,
                                        OrderRequestModel requestModel,
                                         Optional<Integer> limit,
                                         Optional<Integer> offset,
                                         Optional <String> orderBy);
 
    public Order create(ServiceOptions serviceOptions, Order data);
    
    public Order importData(ServiceOptions serviceOptions, Order data, String importHash);

    public Order find(ServiceOptions serviceOptions, String id);

    public List<Order> findAll(ServiceOptions serviceOptions);

    public void delete(ServiceOptions serviceOptions, String id );

    public Order update(ServiceOptions serviceOptions, String id , Order data);
}
