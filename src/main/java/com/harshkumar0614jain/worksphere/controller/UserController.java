package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.model.UserUpdateRequestModel;
import com.harshkumar0614jain.worksphere.model.UserRequestModel;
import com.harshkumar0614jain.worksphere.model.UserResponseModel;
import com.harshkumar0614jain.worksphere.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ResponseModel<List<UserResponseModel>>> getAllUsers(){

        List<UserResponseModel> usersList = userService.getAllUsers();
        ResponseModel<List<UserResponseModel>> response = new ResponseModel<>(
                "Users retrieved successfully", usersList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseModel<UserResponseModel>> getUserById(@PathVariable String userId){

        UserResponseModel user = userService.getUserById(userId);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User fetched successfully", user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseModel<UserResponseModel>> createUser(@Valid @RequestBody UserRequestModel userRequest){

        UserResponseModel userCreated = userService.createUser(userRequest);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User created successfully", userCreated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseModel<UserResponseModel>> updateUser(
            @RequestBody @Valid UserUpdateRequestModel updateRequest,
            @PathVariable String userId){

        UserResponseModel userUpdated = userService.updateUser(updateRequest,userId);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User updated successfully", userUpdated );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseModel<Void>> deleteUser(@PathVariable String userId){

        userService.deleteUser(userId);
        ResponseModel<Void>  response = new ResponseModel<>(
                "User deleted successfully",
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
