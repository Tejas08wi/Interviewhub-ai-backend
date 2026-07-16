package com.interviewhub.controller.recruiter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.auth.RegisterRequest;
import com.interviewhub.dto.auth.RegisterResponse;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.enums.Role;
import com.interviewhub.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterAuthController {

    private final AuthService authService;

    public RecruiterAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerRecruiter(

            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authService.register(
                        request,
                        Role.RECRUITER);

        ApiResponse<RegisterResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Recruiter registered successfully",
                        response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }
}