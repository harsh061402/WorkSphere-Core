package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.User;
import com.harshkumar0614jain.worksphere.enums.Role;
import com.harshkumar0614jain.worksphere.enums.UserStatus;
import com.harshkumar0614jain.worksphere.exception.ResourceAlreadyExistsException;
import com.harshkumar0614jain.worksphere.model.AuthResponse;
import com.harshkumar0614jain.worksphere.model.LoginRequest;
import com.harshkumar0614jain.worksphere.model.RegisterRequest;
import com.harshkumar0614jain.worksphere.model.UserResponseModel;
import com.harshkumar0614jain.worksphere.repository.UserRepository;
import com.harshkumar0614jain.worksphere.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponseModel register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("email","Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("username","Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .roles(Set.of(Role.EMPLOYEE))
                .status(UserStatus.PENDING)
                .build();

        userRepository.save(user);

        return mapToResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
//        Verify Credential
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

//        Generate Token
        String token = jwtService.generateToken(request.getUsername());

        return new  AuthResponse(token);
    }


    private UserResponseModel mapToResponse(User user) {
        return UserResponseModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginDate(user.getLastLoginDate())
                .build();
    }

}
