package com.interviewhub.controller.recruiter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.recruiter.RecruiterProfileRequestDto;
import com.interviewhub.dto.recruiter.RecruiterProfileResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.RecruiterService;
import com.interviewhub.dto.recruiter.RecruiterDashboardResponseDto;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(
            RecruiterService recruiterService) {

        this.recruiterService = recruiterService;
    }

    @PostMapping("/profile")
    public ResponseEntity<String> createRecruiterProfile(
            @Valid @RequestBody RecruiterProfileRequestDto request,
            Authentication authentication) {

        String email = authentication.getName();

        recruiterService.createRecruiterProfile(request, email);

        return ResponseEntity.ok("Recruiter profile created successfully");
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> updateRecruiterProfile(
            @Valid @RequestBody RecruiterProfileRequestDto request,
            Authentication authentication) {

        String email = authentication.getName();

        recruiterService.updateRecruiterProfile(request, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Recruiter profile updated successfully",
                null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<RecruiterProfileResponseDto>> getRecruiterProfile(
            Authentication authentication) {

        String email = authentication.getName();

        RecruiterProfileResponseDto responseDto = recruiterService.getRecruiterProfile(email);

        ApiResponse<RecruiterProfileResponseDto> response = new ApiResponse<>(
                true,
                "Recruiter profile fetched successfully",
                responseDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<RecruiterDashboardResponseDto>> getDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        RecruiterDashboardResponseDto responseDto = recruiterService.getDashboard(email);

        ApiResponse<RecruiterDashboardResponseDto> response = new ApiResponse<>(
                true,
                "Recruiter dashboard fetched successfully",
                responseDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> deleteRecruiterProfile(
            Authentication authentication) {

        String email = authentication.getName();

        recruiterService.deleteRecruiterProfile(email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Recruiter profile deleted successfully",
                null);

        return ResponseEntity.ok(response);
    }

}