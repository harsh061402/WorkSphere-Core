package com.harshkumar0614jain.ems.repository;

import com.harshkumar0614jain.ems.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    boolean existsByUsername(String userName);

    boolean existsByEmail( String email);

    Optional<User> findByUsername(String username);
}
