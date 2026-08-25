/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import com.mp.be.database.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order,String> {
    @Query("{ 'id' : ?0 }")
    Optional<Order> findById(String id);

    @Query(value="{ 'importHash' : ?0 }", count = true)
    Long countImportHash(String importHash);
}