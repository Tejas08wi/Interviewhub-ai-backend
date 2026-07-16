package com.interviewhub.service;

import com.interviewhub.dto.candidate.CandidateProfileRequestDto;
import com.interviewhub.dto.candidate.CandidateProfileResponseDto;

public interface CandidateService {

        void createCandidateProfile(
                        CandidateProfileRequestDto request,
                        String email);

        void updateCandidateProfile(
                        CandidateProfileRequestDto request,
                        String email);

        CandidateProfileResponseDto getCandidateProfile(
                        String email);

         void deleteCandidateProfile(
            String email);

}