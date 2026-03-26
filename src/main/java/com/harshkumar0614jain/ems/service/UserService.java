package com.harshkumar0614jain.ems.service;

import com.harshkumar0614jain.ems.entity.User;
import com.harshkumar0614jain.ems.enums.UserStatus;
import com.harshkumar0614jain.ems.exception.ResourceAlreadyExistsException;
import com.harshkumar0614jain.ems.exception.ResourceNotFoundException;
import com.harshkumar0614jain.ems.model.UserRequestModel;
import com.harshkumar0614jain.ems.model.UserResponseModel;
import com.harshkumar0614jain.ems.model.UserUpdateRequestModel;
import com.harshkumar0614jain.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

    public List<UserResponseModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponseModel getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("userId","User not found with id " + id));
        return mapToResponse(user);
    }

    public UserResponseModel createUser(UserRequestModel requestModel) {

        if(userRepository.existsByUsername(requestModel.getUsername()) )
            throw new ResourceAlreadyExistsException("userName","Username already exists");

        if(userRepository.existsByEmail(requestModel.getEmail()))
            throw new ResourceAlreadyExistsException("email","Email already exists");

        if(requestModel.getRoles().isEmpty())
            throw new RuntimeException("User must have at least one role");

        User user = User.builder()
                .username(requestModel.getUsername())
                .password(passwordEncoder.encode(requestModel.getPassword()))
                .email(requestModel.getEmail())
                .roles(requestModel.getRoles())
                .status(UserStatus.ACTIVE)
                .build();

        User response = userRepository.save(user);

        return mapToResponse(response);
    }


    public UserResponseModel updateUser(
            UserUpdateRequestModel updateRequest, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "userId","User not found with id " + userId));

        if(updateRequest.getRoles()!= null){
            if(updateRequest.getRoles().isEmpty())
                throw new RuntimeException("User must have at least one role");

            user.setRoles(updateRequest.getRoles());
        }

        if(updateRequest.getStatus()!= null)
            user.setStatus(updateRequest.getStatus());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("userId","User not found with id " + userId));

        user.setStatus(UserStatus.DELETED);

        userRepository.save(user);
    }
}
