package com.harshkumar0614jain.worksphere.repository;

import com.harshkumar0614jain.worksphere.entity.User;
import com.harshkumar0614jain.worksphere.enums.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    boolean existsByUsername(String userName);

    boolean existsByEmail( String email);

    Optional<User> findByUsername(String username);

    List<User> findByStatusNot(UserStatus status);

}
