package com.interviewhub.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.auth.LoginRequest;
import com.interviewhub.dto.auth.LoginResponse;
import com.interviewhub.dto.auth.RegisterRequest;
import com.interviewhub.dto.auth.RegisterResponse;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.AuthService;
import com.interviewhub.enums.Role;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(
                request,
                Role.CANDIDATE);

        ApiResponse<RegisterResponse> apiResponse = new ApiResponse<>(
                true,
                "User registered successfully",
                response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(
                true,
                "Login successful",
                response);

        return ResponseEntity.ok(apiResponse);
    }

}