package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.*;
import com.harshkumar0614jain.worksphere.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "APIs for authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register as an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseModel<UserResponseModel>> registerUser(
            @Valid @RequestBody RegisterRequest request){
        UserResponseModel registerUser = authService.register(request);
        ResponseModel<UserResponseModel> response = new ResponseModel<>(
                "User registered successfully", registerUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login and get JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseModel<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse result = authService.login(request);
        ResponseModel<AuthResponse> response = new ResponseModel<>(
                "Login successful", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
