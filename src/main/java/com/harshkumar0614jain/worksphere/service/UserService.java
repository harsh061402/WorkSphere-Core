package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.User;
import com.harshkumar0614jain.worksphere.enums.UserStatus;
import com.harshkumar0614jain.worksphere.exception.BusinessException;
import com.harshkumar0614jain.worksphere.exception.ResourceAlreadyExistsException;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.UserRequestModel;
import com.harshkumar0614jain.worksphere.model.UserResponseModel;
import com.harshkumar0614jain.worksphere.model.UserUpdateRequestModel;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import com.harshkumar0614jain.worksphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    private UserResponseModel mapToResponse(User user) {
        return UserResponseModel.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
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
        return userRepository.findByStatusNot(UserStatus.DELETED)
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
            throw new BusinessException("roles","User must have at least one role");

        User user = User.builder()
                .firstName(requestModel.getFirstName())
                .lastName(requestModel.getLastName())
                .username(requestModel.getUsername())
                .password(passwordEncoder.encode(requestModel.getPassword()))
                .email(requestModel.getEmail())
                .roles(requestModel.getRoles())
                .status(UserStatus.ACTIVE)
                .build();

        User response = userRepository.save(user);

        return mapToResponse(response);
    }

    @Transactional
    public UserResponseModel updateUser(
            UserUpdateRequestModel updateRequest, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "userId","User not found with id " + userId));

//      Check user status is deleted or not
        if(user.getStatus() == UserStatus.DELETED)
            throw new BusinessException("userId",
                    "Cannot update a deleted user");

        if(updateRequest.getRoles()!= null){
            if(updateRequest.getRoles().isEmpty())
                throw new BusinessException("roles",
                        "User must have at least one role");

            user.setRoles(updateRequest.getRoles());
        }

        if(updateRequest.getFirstName() != null)
            user.setFirstName(updateRequest.getFirstName());

        if(updateRequest.getLastName() != null)
            user.setLastName(updateRequest.getLastName());

        if(updateRequest.getFirstName() != null
                || updateRequest.getLastName() != null){

            employeeRepository.findByUserId(userId)
                    .ifPresent(employee ->
                    {
                        if(updateRequest.getFirstName() != null)
                            employee.setFirstName(updateRequest.getFirstName());

                        if(updateRequest.getLastName() != null)
                            employee.setLastName(updateRequest.getLastName());
                        employeeRepository.save(employee);
                    });
        }

        if(updateRequest.getStatus()!= null)
            user.setStatus(updateRequest.getStatus());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "userId","User not found with id " + userId));

//      Check user status is deleted or not
        if(user.getStatus() == UserStatus.DELETED)
            throw new BusinessException("userId",
                    "User is already deleted");

        user.setStatus(UserStatus.DELETED);

        userRepository.save(user);
    }
}
