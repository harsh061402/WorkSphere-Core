package com.harshkumar0614jain.worksphere.seeder;

import com.harshkumar0614jain.worksphere.entity.User;
import com.harshkumar0614jain.worksphere.enums.Role;
import com.harshkumar0614jain.worksphere.enums.UserStatus;
import com.harshkumar0614jain.worksphere.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void run(@NonNull ApplicationArguments args)  {
        if(!userRepository.existsByUsername("admin")
                && !userRepository.existsByEmail("admin@worksphere.com")){
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("Harsh@123"))
                    .email("admin@worksphere.com")
                    .roles(Set.of(Role.ADMIN))
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
        }
    }
}
