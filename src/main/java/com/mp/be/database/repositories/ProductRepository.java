/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import com.mp.be.database.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {
    @Query("{ 'id' : ?0 }")
    Optional<Product> findById(String id);

    @Query(value="{ 'importHash' : ?0 }", count = true)
    Long countImportHash(String importHash);
}