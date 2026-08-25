/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.repositories;

import java.util.Optional;

import com.mp.be.database.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User,String> {


    @Query("{ 'id' : ?0 }")
    Optional<User> findById(String id);

    void deleteById(String id);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordResetToken(String token);

    Optional<User> findByEmailVerificationToken(String token);
}
