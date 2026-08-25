/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import com.mp.be.database.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer,String> {
    @Query("{ 'id' : ?0 }")
    Optional<Customer> findById(String id);

    @Query(value="{ 'importHash' : ?0 }", count = true)
    Long countImportHash(String importHash);
}