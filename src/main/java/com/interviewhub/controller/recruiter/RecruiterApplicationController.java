package com.interviewhub.controller.recruiter;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.application.ApplicationStatusUpdateRequestDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recruiter/jobs")
public class RecruiterApplicationController {

        private final ApplicationService applicationService;

        public RecruiterApplicationController(
                        ApplicationService applicationService) {

                this.applicationService = applicationService;
        }

        @GetMapping("/{jobId}/applications")
        public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getApplicationsForJob(

                        @PathVariable Long jobId,

                        Authentication authentication) {

                String email = authentication.getName();

                List<ApplicationResponseDto> applications = applicationService.getApplicationsForJob(jobId, email);

                ApiResponse<List<ApplicationResponseDto>> response = new ApiResponse<>(
                                true,
                                "Applications fetched successfully",
                                applications);

                return ResponseEntity.ok(response);
        }

        @PutMapping("/applications/{applicationId}/status")
        public ResponseEntity<ApiResponse<Object>> updateApplicationStatus(

                        @PathVariable Long applicationId,

                        @Valid @RequestBody ApplicationStatusUpdateRequestDto request,

                        Authentication authentication) {

                String email = authentication.getName();

                applicationService.updateApplicationStatus(
                                applicationId,
                                request,
                                email);

                ApiResponse<Object> response = new ApiResponse<>(
                                true,
                                "Application status updated successfully",
                                null);

                return ResponseEntity.ok(response);
        }

        @GetMapping("/applications/{applicationId}/resume")
        public ResponseEntity<Resource> downloadCandidateResume(

                        @PathVariable Long applicationId,

                        Authentication authentication) {

                String email = authentication.getName();

                return applicationService.downloadCandidateResume(
                                applicationId,
                                email);
        }

}