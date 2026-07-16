package com.interviewhub.service.impl;

import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.interviewhub.dto.application.ApplicationRequestDto;
import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.application.ApplicationStatusUpdateRequestDto;
import com.interviewhub.entity.JobApplication;
import com.interviewhub.entity.RecruiterProfile;
import com.interviewhub.enums.ApplicationStatus;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.ApplicationRepository;
import com.interviewhub.repository.CandidateResumeRepository;
import com.interviewhub.repository.JobRepository;
import com.interviewhub.repository.RecruiterProfileRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.ApplicationService;
import com.interviewhub.dto.application.ApplicationRequestDto;
import com.interviewhub.entity.CandidateResume;
import com.interviewhub.entity.Job;
import com.interviewhub.entity.JobApplication;
import com.interviewhub.entity.User;
import com.interviewhub.exception.BadRequestException;
import com.interviewhub.exception.ResourceNotFoundException;

@Service
public class ApplicationServiceImpl implements ApplicationService {

        private final ApplicationRepository applicationRepository;
        private final JobRepository jobRepository;
        private final UserRepository userRepository;
        private final RecruiterProfileRepository recruiterProfileRepository;
        private final CandidateResumeRepository candidateResumeRepository;

        public ApplicationServiceImpl(
                        ApplicationRepository applicationRepository,
                        JobRepository jobRepository,
                        UserRepository userRepository,
                        RecruiterProfileRepository recruiterProfileRepository,
                        CandidateResumeRepository candidateResumeRepository) {

                this.applicationRepository = applicationRepository;
                this.jobRepository = jobRepository;
                this.userRepository = userRepository;
                this.recruiterProfileRepository = recruiterProfileRepository;
                this.candidateResumeRepository = candidateResumeRepository;
        }

        @Override
        public void applyForJob(
                        ApplicationRequestDto request,
                        String email) {

                User candidate = getLoggedInUser(email);

                Job job = jobRepository.findById(request.getJobId())
                                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

                if (!Boolean.TRUE.equals(job.getActive())) {
                        throw new BadRequestException("This job is no longer accepting applications");
                }

                if (applicationRepository.existsByCandidateAndJob(candidate, job)) {
                        throw new BadRequestException("You have already applied for this job");
                }

                JobApplication application = new JobApplication();

                application.setCandidate(candidate);
                application.setJob(job);

                applicationRepository.save(application);
        }

        @Override
        public List<ApplicationResponseDto> getMyApplications(String email) {

                User candidate = getLoggedInUser(email);

                return applicationRepository.findByCandidate(candidate)
                                .stream()
                                .map(this::mapToResponseDto)
                                .toList();
        }

        @Override
        public List<ApplicationResponseDto> getApplicationsForJob(
                        Long jobId,
                        String email) {

                User user = getLoggedInUser(email);

                RecruiterProfile recruiterProfile = getRecruiterProfile(user);

                Job job = jobRepository
                                .findByIdAndRecruiterProfile(jobId, recruiterProfile)
                                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

                return applicationRepository
                                .findByJob(job)
                                .stream()
                                .map(this::mapToResponseDto)
                                .toList();
        }

        @Override
        public void updateApplicationStatus(
                        Long applicationId,
                        ApplicationStatusUpdateRequestDto request,
                        String email) {

                User user = getLoggedInUser(email);

                RecruiterProfile recruiterProfile = getRecruiterProfile(user);

                JobApplication application = applicationRepository.findById(applicationId)
                                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

                if (!application.getJob()
                                .getRecruiterProfile()
                                .getId()
                                .equals(recruiterProfile.getId())) {

                        throw new BadRequestException(
                                        "You are not authorized to update this application");
                }

                application.setStatus(request.getStatus());

                applicationRepository.save(application);
        }

        @Override
        public ResponseEntity<Resource> downloadCandidateResume(
                        Long applicationId,
                        String recruiterEmail) {

                User recruiter = userRepository.findByEmail(recruiterEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

                JobApplication application = applicationRepository.findById(applicationId)
                                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

                /*
                 * Security Check
                 */

                if (!application.getJob()
                                .getRecruiterProfile()
                                .getUser()
                                .getId()
                                .equals(recruiter.getId())) {

                        throw new BadRequestException(
                                        "You are not allowed to access this resume");
                }

                User candidate = application.getCandidate();

                CandidateResume resume = candidateResumeRepository
                                .findByUser(candidate)
                                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

                FileSystemResource resource = new FileSystemResource(resume.getFilePath());

                if (!resource.exists()) {
                        throw new ResourceNotFoundException(
                                        "Resume file not found");
                }

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + resume.getFileName() + "\"")
                                .header(
                                                HttpHeaders.CONTENT_TYPE,
                                                resume.getFileType())
                                .body(resource);
        }

        // HELPER METHODS

        private User getLoggedInUser(String email) {

                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        private ApplicationResponseDto mapToResponseDto(
                        JobApplication application) {

                ApplicationResponseDto response = new ApplicationResponseDto();

                response.setApplicationId(application.getId());

                response.setJobId(application.getJob().getId());

                response.setJobTitle(application.getJob().getJobTitle());

                response.setCompanyName(
                                application.getJob()
                                                .getRecruiterProfile()
                                                .getCompanyName());

                response.setCandidateName(
                                application.getCandidate().getFirstName()
                                                + " "
                                                + application.getCandidate().getLastName());

                response.setCandidateEmail(
                                application.getCandidate().getEmail());

                response.setStatus(application.getStatus());

                response.setAppliedAt(application.getAppliedAt());

                return response;
        }

        private RecruiterProfile getRecruiterProfile(User user) {

                return recruiterProfileRepository
                                .findByUser(user)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Recruiter profile not found"));
        }
}