package com.interviewhub.controller.candidate;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.job.JobResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.JobService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/candidate/jobs")
public class CandidateJobController {

    private final JobService jobService;

    public CandidateJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> getAllJobs() {

        List<JobResponseDto> jobs = jobService.getAllActiveJobs();

        ApiResponse<List<JobResponseDto>> response = new ApiResponse<>(
                true,
                "Jobs fetched successfully",
                jobs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponseDto>> getJobById(
            @PathVariable Long jobId) {

        JobResponseDto job = jobService.getActiveJobById(jobId);

        ApiResponse<JobResponseDto> response = new ApiResponse<>(
                true,
                "Job fetched successfully",
                job);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobResponseDto>>> searchJobs(

            @RequestParam(required = false) String title,

            @RequestParam(required = false) String skills,

            @RequestParam(required = false) String location,

            @RequestParam(required = false) String experience) {

        List<JobResponseDto> jobs = jobService.searchJobs(
                title,
                skills,
                location,
                experience);

        ApiResponse<List<JobResponseDto>> response = new ApiResponse<>(
                true,
                "Jobs fetched successfully",
                jobs);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<JobResponseDto>>> getJobsPage(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "jobTitle") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobResponseDto> jobs = jobService.getCandidateJobs(pageable);

        ApiResponse<Page<JobResponseDto>> response = new ApiResponse<>(
                true,
                "Jobs fetched successfully",
                jobs);

        return ResponseEntity.ok(response);
    }
}