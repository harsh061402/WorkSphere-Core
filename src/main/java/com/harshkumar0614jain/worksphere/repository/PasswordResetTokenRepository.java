package com.harshkumar0614jain.worksphere.repository;

import com.harshkumar0614jain.worksphere.entity.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends
        MongoRepository<PasswordResetToken,String> {

    Optional<PasswordResetToken> findByToken(String token);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

}
