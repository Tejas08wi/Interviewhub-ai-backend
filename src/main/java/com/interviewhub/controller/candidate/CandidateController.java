package com.interviewhub.controller.candidate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.candidate.CandidateProfileRequestDto;
import com.interviewhub.dto.candidate.CandidateProfileResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.CandidateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(
            CandidateService candidateService) {

        this.candidateService = candidateService;
    }

    @PostMapping("/profile")
    public ResponseEntity<String> createCandidateProfile(
            @Valid @RequestBody CandidateProfileRequestDto request,
            Authentication authentication) {

        String email = authentication.getName();

        candidateService.createCandidateProfile(request, email);

        return ResponseEntity.ok("Candidate profile created successfully");
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> updateCandidateProfile(
            @Valid @RequestBody CandidateProfileRequestDto request,
            Authentication authentication) {

        String email = authentication.getName();

        candidateService.updateCandidateProfile(request, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Candidate profile updated successfully",
                null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<CandidateProfileResponseDto>> getCandidateProfile(Authentication authentication) {

        String email = authentication.getName();

        CandidateProfileResponseDto responseDto = candidateService.getCandidateProfile(email);

        ApiResponse<CandidateProfileResponseDto> response = new ApiResponse<>(
                true,
                "Candidate profile fetched successfully",
                responseDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> deleteCandidateProfile(
            Authentication authentication) {

        String email = authentication.getName();

        candidateService.deleteCandidateProfile(email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Candidate profile deleted successfully",
                null);

        return ResponseEntity.ok(response);
    }

}