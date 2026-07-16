package com.interviewhub.controller.ai;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.ai.ResumeAnalysisResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.ResumeAnalysisService;

@RestController
@RequestMapping("/ai/resume")
public class ResumeAnalysisController {

        private final ResumeAnalysisService resumeAnalysisService;

        public ResumeAnalysisController(
                        ResumeAnalysisService resumeAnalysisService) {

                this.resumeAnalysisService = resumeAnalysisService;
        }

        @PostMapping("/analyze")
        public ResponseEntity<ApiResponse<ResumeAnalysisResponseDto>> analyzeResume(Authentication authentication) {

                String email = authentication.getName();

                ResumeAnalysisResponseDto result = resumeAnalysisService.analyzeResume(email);

                ApiResponse<ResumeAnalysisResponseDto> response = new ApiResponse<>(
                                true,
                                "Resume analyzed successfully",
                                result);

                return ResponseEntity.ok(response);
        }

}