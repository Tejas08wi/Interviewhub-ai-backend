package com.interviewhub.controller.recruiter;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.job.JobRequestDto;
import com.interviewhub.dto.job.JobResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recruiter/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createJob(
            @Valid @RequestBody JobRequestDto request,
            Authentication authentication) {

        String email = authentication.getName();

        jobService.createJob(request, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Job created successfully",
                null);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getAllJobs(
            Authentication authentication) {

        String email = authentication.getName();

        List<JobResponseDto> jobs = jobService.getAllJobs(email);

        ApiResponse<List<JobResponseDto>> response = new ApiResponse<>(
                true,
                "Jobs fetched successfully",
                jobs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponseDto>> getJobById(
            @PathVariable Long jobId,
            Authentication authentication) {

        String email = authentication.getName();

        JobResponseDto job = jobService.getJobById(jobId, email);

        ApiResponse<JobResponseDto> response = new ApiResponse<>(
                true,
                "Job fetched successfully",
                job);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Object>> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequestDto request,
            Authentication authentication) {

        String email = authentication.getName();

        jobService.updateJob(jobId, request, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Job updated successfully",
                null);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Object>> deleteJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String email = authentication.getName();

        jobService.deleteJob(jobId, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Job deleted successfully",
                null);

        return ResponseEntity.ok(response);
    }
     @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<JobResponseDto>>> getJobsPage(
            Authentication authentication,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        String email = authentication.getName();

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobResponseDto> jobs = jobService.getRecruiterJobs(email, pageable);

        ApiResponse<Page<JobResponseDto>> response = new ApiResponse<>(
                true,
                "Jobs fetched successfully",
                jobs);

        return ResponseEntity.ok(response);
    }
}