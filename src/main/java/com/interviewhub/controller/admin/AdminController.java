package com.interviewhub.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewhub.dto.admin.AdminDashboardResponseDto;
import com.interviewhub.dto.admin.AdminInterviewDetailsResponseDto;
import com.interviewhub.dto.admin.AdminRecruiterListResponseDto;
import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.candidate.CandidateProfileResponseDto;
import com.interviewhub.dto.interview.InterviewHistoryResponseDto;
import com.interviewhub.dto.job.JobResponseDto;
import com.interviewhub.dto.recruiter.RecruiterProfileResponseDto;
import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.AdminService;
import java.util.List;

import com.interviewhub.dto.admin.AdminCandidateListResponseDto;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

        private final AdminService adminService;

        public AdminController(AdminService adminService) {
                this.adminService = adminService;
        }

        @GetMapping("/dashboard")
        public ResponseEntity<ApiResponse<AdminDashboardResponseDto>> getDashboard() {

                AdminDashboardResponseDto response = adminService.getDashboard();

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Admin dashboard fetched successfully.",
                                                response));
        }

        @GetMapping("/candidates")
        public ResponseEntity<ApiResponse<List<AdminCandidateListResponseDto>>> getAllCandidates() {

                List<AdminCandidateListResponseDto> candidates = adminService.getAllCandidates();

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Candidates fetched successfully.",
                                                candidates));
        }

        @GetMapping("/candidates/{candidateId}")
        public ResponseEntity<ApiResponse<CandidateProfileResponseDto>> getCandidateById(
                        @PathVariable Long candidateId) {

                CandidateProfileResponseDto response = adminService.getCandidateById(candidateId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Candidate fetched successfully.",
                                                response));
        }

        @DeleteMapping("/candidates/{candidateId}")
        public ResponseEntity<ApiResponse<Object>> deleteCandidate(
                        @PathVariable Long candidateId) {

                adminService.deleteCandidate(candidateId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Candidate deleted successfully.",
                                                null));
        }

        @GetMapping("/recruiters")
        public ResponseEntity<ApiResponse<List<AdminRecruiterListResponseDto>>> getAllRecruiters() {

                List<AdminRecruiterListResponseDto> recruiters = adminService.getAllRecruiters();

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Recruiters fetched successfully.",
                                                recruiters));
        }

        @GetMapping("/recruiters/{recruiterId}")
        public ResponseEntity<ApiResponse<RecruiterProfileResponseDto>> getRecruiterById(
                        @PathVariable Long recruiterId) {

                RecruiterProfileResponseDto response = adminService.getRecruiterById(recruiterId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Recruiter fetched successfully.",
                                                response));
        }

        @DeleteMapping("/recruiters/{recruiterId}")
        public ResponseEntity<ApiResponse<Object>> deleteRecruiter(
                        @PathVariable Long recruiterId) {

                adminService.deleteRecruiter(recruiterId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Recruiter deleted successfully.",
                                                null));
        }

        @GetMapping("/jobs")
        public ResponseEntity<ApiResponse<List<JobResponseDto>>> getAllJobs() {

                List<JobResponseDto> jobs = adminService.getAllJobs();

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Jobs fetched successfully.",
                                                jobs));
        }

        @GetMapping("/jobs/{jobId}")
        public ResponseEntity<ApiResponse<JobResponseDto>> getJobById(
                        @PathVariable Long jobId) {

                JobResponseDto response = adminService.getJobById(jobId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Job fetched successfully.",
                                                response));
        }

        @DeleteMapping("/jobs/{jobId}")
        public ResponseEntity<ApiResponse<Object>> deleteJob(
                        @PathVariable Long jobId) {

                adminService.deleteJob(jobId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Job deleted successfully.",
                                                null));
        }

        @GetMapping("/applications")
        public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllApplications() {

                List<ApplicationResponseDto> applications = adminService.getAllApplications();

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Applications fetched successfully.",
                                                applications));
        }

        @GetMapping("/applications/{applicationId}")
        public ResponseEntity<ApiResponse<ApplicationResponseDto>> getApplicationById(
                        @PathVariable Long applicationId) {

                ApplicationResponseDto response = adminService.getApplicationById(applicationId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Application fetched successfully.",
                                                response));
        }

        @DeleteMapping("/applications/{applicationId}")
        public ResponseEntity<ApiResponse<Object>> deleteApplication(
                        @PathVariable Long applicationId) {

                adminService.deleteApplication(applicationId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Application deleted successfully.",
                                                null));
        }

        @GetMapping("/interviews")
        public ResponseEntity<ApiResponse<List<InterviewHistoryResponseDto>>> getAllInterviews() {

                List<InterviewHistoryResponseDto> interviews = adminService.getAllInterviews();

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Interviews fetched successfully.",
                                                interviews));
        }

        @GetMapping("/interviews/{sessionId}")
        public ResponseEntity<ApiResponse<AdminInterviewDetailsResponseDto>> getInterviewById(
                        @PathVariable Long sessionId) {

                AdminInterviewDetailsResponseDto response = adminService.getInterviewById(sessionId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Interview fetched successfully.",
                                                response));
        }

        @DeleteMapping("/interviews/{sessionId}")
        public ResponseEntity<ApiResponse<Object>> deleteInterview(
                        @PathVariable Long sessionId) {

                adminService.deleteInterview(sessionId);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Interview deleted successfully.",
                                                null));
        }
}