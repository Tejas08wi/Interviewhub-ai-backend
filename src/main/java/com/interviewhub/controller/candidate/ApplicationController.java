package com.interviewhub.controller.candidate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.application.ApplicationRequestDto;
import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.ApplicationService;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate/applications")
@Validated
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> applyForJob(

            @Valid @RequestBody ApplicationRequestDto request,

            Authentication authentication) {

        String email = authentication.getName();

        applicationService.applyForJob(request, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Job applied successfully",
                null);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getMyApplications(
            Authentication authentication) {

        String email = authentication.getName();

        List<ApplicationResponseDto> applications = applicationService.getMyApplications(email);

        ApiResponse<List<ApplicationResponseDto>> response = new ApiResponse<>(
                true,
                "Applications fetched successfully",
                applications);

        return ResponseEntity.ok(response);
    }

}