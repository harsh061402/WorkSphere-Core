package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.model.UserUpdateRequestModel;
import com.harshkumar0614jain.worksphere.model.UserRequestModel;
import com.harshkumar0614jain.worksphere.model.UserResponseModel;
import com.harshkumar0614jain.worksphere.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "APIs for managing users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseModel<List<UserResponseModel>>> getAllUsers(){

        List<UserResponseModel> usersList = userService.getAllUsers();
        ResponseModel<List<UserResponseModel>> response = new ResponseModel<>(
                "Users retrieved successfully", usersList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseModel<UserResponseModel>> getUserById(@PathVariable String userId){

        UserResponseModel user = userService.getUserById(userId);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User fetched successfully", user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseModel<UserResponseModel>> createUser(@Valid @RequestBody UserRequestModel userRequest){

        UserResponseModel userCreated = userService.createUser(userRequest);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User created successfully", userCreated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseModel<UserResponseModel>> updateUser(
            @RequestBody @Valid UserUpdateRequestModel updateRequest,
            @PathVariable String userId){

        UserResponseModel userUpdated = userService.updateUser(updateRequest,userId);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User updated successfully", userUpdated );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseModel<Void>> deleteUser(@PathVariable String userId){

        userService.deleteUser(userId);
        ResponseModel<Void>  response = new ResponseModel<>(
                "User deleted successfully",
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
