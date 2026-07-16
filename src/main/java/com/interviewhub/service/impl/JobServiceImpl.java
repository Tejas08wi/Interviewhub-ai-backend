package com.interviewhub.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.job.JobRequestDto;
import com.interviewhub.dto.job.JobResponseDto;
import com.interviewhub.entity.Job;
import com.interviewhub.entity.RecruiterProfile;
import com.interviewhub.entity.User;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.JobRepository;
import com.interviewhub.repository.RecruiterProfileRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.JobService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public JobServiceImpl(
            JobRepository jobRepository,
            UserRepository userRepository,
            RecruiterProfileRepository recruiterProfileRepository) {

        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public void createJob(JobRequestDto request, String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile recruiterProfile = getRecruiterProfile(user);

        Job job = new Job();

        mapRequestToJob(request, job);

        job.setRecruiterProfile(recruiterProfile);

        jobRepository.save(job);
    }

    @Override
    public List<JobResponseDto> getAllJobs(String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile recruiterProfile = getRecruiterProfile(user);

        return jobRepository.findByRecruiterProfile(recruiterProfile)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobResponseDto getJobById(Long jobId, String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile recruiterProfile = getRecruiterProfile(user);

        Job job = jobRepository.findByIdAndRecruiterProfile(jobId, recruiterProfile)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        return mapToResponseDto(job);
    }

    @Override
    public void updateJob(Long jobId, JobRequestDto request, String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile recruiterProfile = getRecruiterProfile(user);

        Job job = jobRepository.findByIdAndRecruiterProfile(jobId, recruiterProfile)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        mapRequestToJob(request, job);

        jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long jobId, String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile recruiterProfile = getRecruiterProfile(user);

        Job job = jobRepository.findByIdAndRecruiterProfile(jobId, recruiterProfile)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        jobRepository.delete(job);
    }

    @Override
    public List<JobResponseDto> getAllActiveJobs() {

        return jobRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public JobResponseDto getActiveJobById(Long jobId) {

        Job job = jobRepository.findByIdAndActiveTrue(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        return mapToResponseDto(job);
    }

    @Override
    public List<JobResponseDto> searchJobs(
            String title,
            String skills,
            String location,
            String experience) {

        return jobRepository.searchJobs(
                title,
                skills,
                location,
                experience)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<JobResponseDto> getRecruiterJobs(
            String email,
            Pageable pageable) {

        User user = getLoggedInUser(email);

        RecruiterProfile recruiterProfile = getRecruiterProfile(user);

        return jobRepository
                .findByRecruiterProfile(recruiterProfile, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public Page<JobResponseDto> getCandidateJobs(
            Pageable pageable) {

        return jobRepository
                .findByActiveTrue(pageable)
                .map(this::mapToResponseDto);
    }

    // ==========================
    // Helper Methods
    // ==========================

    private User getLoggedInUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private RecruiterProfile getRecruiterProfile(User user) {

        return recruiterProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
    }

    private void mapRequestToJob(JobRequestDto request, Job job) {

        job.setJobTitle(request.getJobTitle());
        job.setJobDescription(request.getJobDescription());
        job.setSkills(request.getSkills());
        job.setExperience(request.getExperience());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());

        if (request.getActive() != null) {
            job.setActive(request.getActive());
        }
    }

    private JobResponseDto mapToResponseDto(Job job) {

        JobResponseDto response = new JobResponseDto();

        response.setId(job.getId());
        response.setJobTitle(job.getJobTitle());
        response.setJobDescription(job.getJobDescription());
        response.setSkills(job.getSkills());
        response.setExperience(job.getExperience());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setMinSalary(job.getMinSalary());
        response.setMaxSalary(job.getMaxSalary());
        response.setActive(job.getActive());

        response.setCompanyName(job.getRecruiterProfile().getCompanyName());

        User user = job.getRecruiterProfile().getUser();

        response.setRecruiterName(
                user.getFirstName() + " " + user.getLastName());

        return response;
    }
}