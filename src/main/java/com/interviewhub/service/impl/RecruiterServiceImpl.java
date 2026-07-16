package com.interviewhub.service.impl;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.recruiter.RecruiterProfileRequestDto;
import com.interviewhub.dto.recruiter.RecruiterProfileResponseDto;
import com.interviewhub.entity.RecruiterProfile;
import com.interviewhub.entity.User;
import com.interviewhub.exception.BadRequestException;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.RecruiterProfileRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.RecruiterService;
import com.interviewhub.dto.recruiter.RecruiterDashboardResponseDto;
import com.interviewhub.repository.CandidateProfileRepository;
import com.interviewhub.repository.InterviewEvaluationRepository;
import com.interviewhub.repository.InterviewSessionRepository;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final InterviewSessionRepository interviewSessionRepository;
    private final InterviewEvaluationRepository interviewEvaluationRepository;
    

    public RecruiterServiceImpl(
            UserRepository userRepository,
            RecruiterProfileRepository recruiterProfileRepository,
            CandidateProfileRepository candidateProfileRepository,
            InterviewSessionRepository interviewSessionRepository,
            InterviewEvaluationRepository interviewEvaluationRepository) {

        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.interviewSessionRepository = interviewSessionRepository;
        this.interviewEvaluationRepository = interviewEvaluationRepository;
    }

    @Override
    public void createRecruiterProfile(
            RecruiterProfileRequestDto request,
            String email) {

        User user = getLoggedInUser(email);

        if (recruiterProfileRepository.existsByUser(user)) {
            throw new BadRequestException("Recruiter profile already exists");
        }

        RecruiterProfile profile = new RecruiterProfile();

        mapRequestToProfile(request, profile);

        profile.setUser(user);

        recruiterProfileRepository.save(profile);
    }

    @Override
    public void updateRecruiterProfile(
            RecruiterProfileRequestDto request,
            String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile profile = getRecruiterProfile(user);

        mapRequestToProfile(request, profile);

        recruiterProfileRepository.save(profile);
    }

    @Override
    public RecruiterProfileResponseDto getRecruiterProfile(
            String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile profile = getRecruiterProfile(user);

        RecruiterProfileResponseDto response = new RecruiterProfileResponseDto();

        response.setId(profile.getId());

        response.setCompanyName(profile.getCompanyName());
        response.setDesignation(profile.getDesignation());
        response.setCompanyWebsite(profile.getCompanyWebsite());
        response.setCompanyDescription(profile.getCompanyDescription());
        response.setIndustry(profile.getIndustry());
        response.setLocation(profile.getLocation());
        response.setProfilePhoto(profile.getProfilePhoto());

        response.setRecruiterName(
                user.getFirstName() + " " + user.getLastName());

        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public void deleteRecruiterProfile(
            String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile profile = getRecruiterProfile(user);

        recruiterProfileRepository.delete(profile);
    }

    @Override
    public RecruiterDashboardResponseDto getDashboard(String email) {

        User user = getLoggedInUser(email);

        RecruiterProfile profile = getRecruiterProfile(user);

        RecruiterDashboardResponseDto response = new RecruiterDashboardResponseDto();

        response.setRecruiterName(
                user.getFirstName() + " " + user.getLastName());

        response.setEmail(user.getEmail());

        response.setCompanyName(profile.getCompanyName());

        response.setDesignation(profile.getDesignation());

        response.setTotalCandidates(candidateProfileRepository.count());

        response.setTotalInterviewSessions(interviewSessionRepository.count());

        response.setCompletedEvaluations(interviewEvaluationRepository.count());

        Double averageScore = interviewEvaluationRepository.getAverageOverallScore();

        response.setAverageInterviewScore(
                averageScore == null ? 0.0 : averageScore);

        return response;
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

    private void mapRequestToProfile(
            RecruiterProfileRequestDto request,
            RecruiterProfile profile) {

        profile.setCompanyName(request.getCompanyName());
        profile.setDesignation(request.getDesignation());
        profile.setCompanyWebsite(request.getCompanyWebsite());
        profile.setCompanyDescription(request.getCompanyDescription());
        profile.setIndustry(request.getIndustry());
        profile.setLocation(request.getLocation());
        profile.setProfilePhoto(request.getProfilePhoto());
    }
}