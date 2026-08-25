package com.mp.be.database.repositories;

import com.mp.be.database.entities.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface FileRepository extends MongoRepository<File,String> {

    @Query("{ 'id' : ?0 }")
    Optional<File> findById(String id);
}
