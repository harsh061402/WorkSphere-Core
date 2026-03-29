package com.harshkumar0614jain.ems.controller;

import com.harshkumar0614jain.ems.model.ResponseModel;
import com.harshkumar0614jain.ems.model.UserUpdateRequestModel;
import com.harshkumar0614jain.ems.model.UserRequestModel;
import com.harshkumar0614jain.ems.model.UserResponseModel;
import com.harshkumar0614jain.ems.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ResponseModel<List<UserResponseModel>>> getAllUser(){

        List<UserResponseModel> usersList = userService.getAllUsers();
        ResponseModel<List<UserResponseModel>> response = new ResponseModel<>(
                "List of users", usersList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseModel<UserResponseModel>> getUser(@PathVariable String userId){

        UserResponseModel user = userService.getUserById(userId);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User is found", user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseModel<UserResponseModel>> createUser(@Valid @RequestBody UserRequestModel userRequest){

        UserResponseModel userCreated = userService.createUser(userRequest);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User is created successfully", userCreated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<ResponseModel<UserResponseModel>> updateUser(
            @RequestBody @Valid UserUpdateRequestModel updateRequest,
            @PathVariable String userId){

        UserResponseModel userUpdated = userService.updateUser(updateRequest,userId);
        ResponseModel<UserResponseModel>  response = new ResponseModel<>(
                "User is updated successfully", userUpdated );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseModel<Void>> deleteUser(@PathVariable String userId){

        userService.deleteUser(userId);
        ResponseModel<Void>  response = new ResponseModel<>(
                "User is deleted successfully",
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
