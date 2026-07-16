package com.interviewhub.service.impl;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.candidate.CandidateProfileRequestDto;
import com.interviewhub.dto.candidate.CandidateProfileResponseDto;
import com.interviewhub.entity.CandidateProfile;
import com.interviewhub.entity.User;
import com.interviewhub.exception.BadRequestException;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.CandidateProfileRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateServiceImpl(
            UserRepository userRepository,
            CandidateProfileRepository candidateProfileRepository) {

        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Override
    public void createCandidateProfile(
            CandidateProfileRequestDto request,
            String email) {

        User user = getLoggedInUser(email);

        if (candidateProfileRepository.existsByUser(user)) {
            throw new BadRequestException("Candidate profile already exists");
        }

        CandidateProfile profile = new CandidateProfile();

        mapRequestToProfile(request, profile);

        profile.setUser(user);

        candidateProfileRepository.save(profile);
    }

    @Override
    public void updateCandidateProfile(
            CandidateProfileRequestDto request,
            String email) {

        User user = getLoggedInUser(email);

        CandidateProfile profile = getCandidateProfile(user);

        mapRequestToProfile(request, profile);

        candidateProfileRepository.save(profile);
    }

    @Override
    public CandidateProfileResponseDto getCandidateProfile(
            String email) {

        User user = getLoggedInUser(email);

        CandidateProfile profile = getCandidateProfile(user);

        CandidateProfileResponseDto response = new CandidateProfileResponseDto();

        response.setId(profile.getId());

        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());

        response.setPhone(profile.getPhone());
        response.setHeadline(profile.getHeadline());
        response.setAbout(profile.getAbout());
        response.setExperience(profile.getExperience());
        response.setEducation(profile.getEducation());
        response.setSkills(profile.getSkills());
        response.setLocation(profile.getLocation());
        response.setGithub(profile.getGithub());
        response.setLinkedin(profile.getLinkedin());
        response.setPortfolio(profile.getPortfolio());
        response.setProfilePhoto(profile.getProfilePhoto());

        return response;
    }

    @Override
    public void deleteCandidateProfile(String email) {

        User user = getLoggedInUser(email);

        CandidateProfile profile = getCandidateProfile(user);

        candidateProfileRepository.delete(profile);
    }

    // ==========================
    // Helper Methods
    // ==========================

    private User getLoggedInUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private CandidateProfile getCandidateProfile(User user) {

        return candidateProfileRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidate profile not found"));
    }

    private void mapRequestToProfile(
            CandidateProfileRequestDto request,
            CandidateProfile profile) {

        profile.setPhone(request.getPhone());
        profile.setHeadline(request.getHeadline());
        profile.setAbout(request.getAbout());
        profile.setExperience(request.getExperience());
        profile.setEducation(request.getEducation());
        profile.setSkills(request.getSkills());
        profile.setLocation(request.getLocation());
        profile.setGithub(request.getGithub());
        profile.setLinkedin(request.getLinkedin());
        profile.setPortfolio(request.getPortfolio());
        profile.setProfilePhoto(request.getProfilePhoto());
    }
}