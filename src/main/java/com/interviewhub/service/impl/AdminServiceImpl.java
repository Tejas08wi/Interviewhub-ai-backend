package com.interviewhub.service.impl;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.admin.AdminDashboardResponseDto;
import com.interviewhub.dto.admin.AdminInterviewDetailsResponseDto;
import com.interviewhub.dto.admin.AdminRecruiterListResponseDto;
import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.candidate.CandidateProfileResponseDto;
import com.interviewhub.dto.interview.InterviewHistoryResponseDto;
import com.interviewhub.dto.job.JobResponseDto;
import com.interviewhub.dto.recruiter.RecruiterProfileResponseDto;
import com.interviewhub.repository.ApplicationRepository;
import com.interviewhub.repository.CandidateProfileRepository;
import com.interviewhub.repository.CandidateResumeRepository;
import com.interviewhub.repository.InterviewSessionRepository;
import com.interviewhub.repository.JobRepository;
import com.interviewhub.repository.RecruiterProfileRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.AdminService;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.interviewhub.entity.CandidateResume;

import com.interviewhub.dto.admin.AdminCandidateListResponseDto;
import com.interviewhub.entity.CandidateProfile;
import com.interviewhub.entity.CandidateResume;
import com.interviewhub.entity.User;
import com.interviewhub.enums.Role;
import com.interviewhub.exception.ResourceNotFoundException;

import com.interviewhub.repository.InterviewAnswerRepository;
import com.interviewhub.repository.InterviewEvaluationRepository;
import com.interviewhub.repository.InterviewQuestionRepository;
import org.springframework.transaction.annotation.Transactional;

import com.interviewhub.entity.InterviewAnswer;
import com.interviewhub.entity.InterviewEvaluation;
import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;
import com.interviewhub.entity.Job;
import com.interviewhub.entity.JobApplication;
import com.interviewhub.entity.RecruiterProfile;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewSessionRepository interviewSessionRepository;
    private final CandidateResumeRepository candidateResumeRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final InterviewEvaluationRepository interviewEvaluationRepository;

    public AdminServiceImpl(
            UserRepository userRepository,
            CandidateProfileRepository candidateProfileRepository,
            RecruiterProfileRepository recruiterProfileRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository,
            InterviewSessionRepository interviewSessionRepository,
            CandidateResumeRepository candidateResumeRepository,
            InterviewQuestionRepository interviewQuestionRepository,
            InterviewAnswerRepository interviewAnswerRepository,
            InterviewEvaluationRepository interviewEvaluationRepository) {

        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewSessionRepository = interviewSessionRepository;
        this.candidateResumeRepository = candidateResumeRepository;
        this.interviewQuestionRepository = interviewQuestionRepository;
        this.interviewAnswerRepository = interviewAnswerRepository;
        this.interviewEvaluationRepository = interviewEvaluationRepository;
    }

    @Override
    public AdminDashboardResponseDto getDashboard() {

        AdminDashboardResponseDto response = new AdminDashboardResponseDto();

        response.setTotalUsers(userRepository.count());
        response.setTotalCandidates(candidateProfileRepository.count());
        response.setTotalRecruiters(recruiterProfileRepository.count());
        response.setTotalJobs(jobRepository.count());
        response.setTotalApplications(applicationRepository.count());
        response.setTotalInterviewSessions(interviewSessionRepository.count());
        response.setTotalResumes(candidateResumeRepository.count());

        return response;
    }

    @Override
    public List<AdminCandidateListResponseDto> getAllCandidates() {

        List<User> candidates = userRepository.findByRole(Role.CANDIDATE);

        List<AdminCandidateListResponseDto> responseList = new ArrayList<>();

        for (User user : candidates) {

            AdminCandidateListResponseDto dto = new AdminCandidateListResponseDto();

            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());

            candidateProfileRepository.findByUser(user).ifPresent(profile -> {
                dto.setPhone(profile.getPhone());
                dto.setHeadline(profile.getHeadline());
                dto.setLocation(profile.getLocation());
            });

            responseList.add(dto);
        }

        return responseList;
    }

    @Override
    public CandidateProfileResponseDto getCandidateById(Long candidateId) {

        User user = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (user.getRole() != Role.CANDIDATE) {
            throw new ResourceNotFoundException("Candidate not found");
        }

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

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
    @Transactional
    public void deleteCandidate(Long candidateId) {

        User user = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (user.getRole() != Role.CANDIDATE) {
            throw new ResourceNotFoundException("Candidate not found");
        }

        // Delete Interview Data
        List<InterviewSession> sessions = interviewSessionRepository.findByUser(user);

        for (InterviewSession session : sessions) {

            List<InterviewQuestion> questions = interviewQuestionRepository.findByInterviewSession(session);

            for (InterviewQuestion question : questions) {

                interviewAnswerRepository.findByInterviewQuestion(question)
                        .ifPresent(answer -> interviewAnswerRepository.delete(answer));
            }

            interviewQuestionRepository.deleteByInterviewSession(session);

            interviewEvaluationRepository.findByInterviewSession(session)
                    .ifPresent(evaluation -> interviewEvaluationRepository.delete(evaluation));
        }

        interviewSessionRepository.deleteByUser(user);

        // Delete Job Applications
        applicationRepository.deleteByCandidate(user);

        // Delete Resume
        // Delete Resume
        if (candidateResumeRepository.existsByUser(user)) {

            CandidateResume resume = candidateResumeRepository
                    .findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

            try {

                Files.deleteIfExists(Paths.get(resume.getFilePath()));

            } catch (IOException e) {

                throw new RuntimeException("Failed to delete resume file.");
            }

            candidateResumeRepository.delete(resume);
        }

        // Delete Candidate Profile
        if (candidateProfileRepository.existsByUser(user)) {
            candidateProfileRepository.delete(
                    candidateProfileRepository.findByUser(user).get());
        }

        // Delete User
        userRepository.delete(user);
    }

    @Override
    public List<AdminRecruiterListResponseDto> getAllRecruiters() {

        List<User> recruiters = userRepository.findByRole(Role.RECRUITER);

        List<AdminRecruiterListResponseDto> responseList = new ArrayList<>();

        for (User user : recruiters) {

            AdminRecruiterListResponseDto dto = new AdminRecruiterListResponseDto();

            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());

            recruiterProfileRepository.findByUser(user).ifPresent(profile -> {

                dto.setCompanyName(profile.getCompanyName());
                dto.setDesignation(profile.getDesignation());
                dto.setIndustry(profile.getIndustry());
                dto.setLocation(profile.getLocation());

            });

            responseList.add(dto);
        }

        return responseList;
    }

    @Override
    public RecruiterProfileResponseDto getRecruiterById(Long recruiterId) {

        User user = userRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        if (user.getRole() != Role.RECRUITER) {
            throw new ResourceNotFoundException("Recruiter not found");
        }

        RecruiterProfile profile = recruiterProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));

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
    @Transactional
    public void deleteRecruiter(Long recruiterId) {

        User user = userRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        if (user.getRole() != Role.RECRUITER) {
            throw new ResourceNotFoundException("Recruiter not found");
        }

        Optional<RecruiterProfile> optionalRecruiterProfile = recruiterProfileRepository.findByUser(user);

        if (optionalRecruiterProfile.isPresent()) {

            RecruiterProfile recruiterProfile = optionalRecruiterProfile.get();

            // Delete Applications of all Jobs
            List<Job> jobs = jobRepository.findByRecruiterProfile(recruiterProfile);

            for (Job job : jobs) {
                applicationRepository.deleteByJob(job);
            }

            // Delete Jobs
            jobRepository.deleteAll(jobs);

            // Delete Recruiter Profile
            recruiterProfileRepository.delete(recruiterProfile);
        }

        // Delete User (always)
        userRepository.delete(user);
    }

    @Override
    public List<JobResponseDto> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::mapJobToResponseDto)
                .toList();
    }

    private JobResponseDto mapJobToResponseDto(Job job) {

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

        User recruiter = job.getRecruiterProfile().getUser();

        response.setRecruiterName(
                recruiter.getFirstName() + " " + recruiter.getLastName());

        return response;
    }

    @Override
    public JobResponseDto getJobById(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        return mapJobToResponseDto(job);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Delete all applications for this job
        applicationRepository.deleteByJob(job);

        // Delete the job
        jobRepository.delete(job);
    }

    @Override
    public List<ApplicationResponseDto> getAllApplications() {

        return applicationRepository.findAll()
                .stream()
                .map(this::mapApplicationToResponseDto)
                .toList();
    }

    private ApplicationResponseDto mapApplicationToResponseDto(
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

    @Override
    public ApplicationResponseDto getApplicationById(Long applicationId) {

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        return mapApplicationToResponseDto(application);
    }

    @Override
    @Transactional
    public void deleteApplication(Long applicationId) {

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        applicationRepository.delete(application);
    }

    @Override
    public List<InterviewHistoryResponseDto> getAllInterviews() {

        return interviewSessionRepository.findAll()
                .stream()
                .map(this::mapInterviewHistoryDto)
                .toList();
    }

    private InterviewHistoryResponseDto mapInterviewHistoryDto(
            InterviewSession session) {

        Double overallScore = interviewEvaluationRepository
                .findByInterviewSession(session)
                .map(InterviewEvaluation::getOverallScore)
                .orElse(null);

        return new InterviewHistoryResponseDto(
                session.getId(),
                session.getJobRole(),
                session.getDifficulty(),
                session.getTotalQuestions(),
                overallScore,
                session.getCreatedAt());
    }

    @Override
    public AdminInterviewDetailsResponseDto getInterviewById(Long sessionId) {

        InterviewSession session = interviewSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found"));

        AdminInterviewDetailsResponseDto response = new AdminInterviewDetailsResponseDto();

        response.setSessionId(session.getId());

        response.setCandidateName(
                session.getUser().getFirstName()
                        + " "
                        + session.getUser().getLastName());

        response.setCandidateEmail(session.getUser().getEmail());

        response.setJobRole(session.getJobRole());

        response.setDifficulty(session.getDifficulty());

        response.setTotalQuestions(session.getTotalQuestions());

        response.setCreatedAt(session.getCreatedAt());

        interviewEvaluationRepository.findByInterviewSession(session)
                .ifPresent(evaluation -> {

                    response.setOverallScore(evaluation.getOverallScore());
                    response.setTechnicalScore(evaluation.getTechnicalScore());
                    response.setCommunicationScore(evaluation.getCommunicationScore());
                    response.setStrengths(evaluation.getStrengths());
                    response.setWeaknesses(evaluation.getWeaknesses());
                    response.setSuggestions(evaluation.getSuggestions());
                    response.setFinalFeedback(evaluation.getFinalFeedback());

                });

        List<AdminInterviewDetailsResponseDto.QuestionDetails> questionList = interviewQuestionRepository
                .findByInterviewSession(session)
                .stream()
                .map(this::mapQuestionDetails)
                .toList();

        response.setQuestions(questionList);

        return response;
    }

    private AdminInterviewDetailsResponseDto.QuestionDetails mapQuestionDetails(
            InterviewQuestion question) {

        AdminInterviewDetailsResponseDto.QuestionDetails dto = new AdminInterviewDetailsResponseDto.QuestionDetails();

        dto.setQuestionNumber(question.getQuestionNumber());

        dto.setCategory(question.getCategory());

        dto.setQuestion(question.getQuestion());

        dto.setExpectedAnswer(question.getExpectedAnswer());

        interviewAnswerRepository.findByInterviewQuestion(question)
                .ifPresent(answer -> dto.setCandidateAnswer(answer.getCandidateAnswer()));

        return dto;
    }

    @Override
    @Transactional
    public void deleteInterview(Long sessionId) {

        InterviewSession session = interviewSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found"));

        // Delete Interview Answers
        List<InterviewQuestion> questions = interviewQuestionRepository.findByInterviewSession(session);

        for (InterviewQuestion question : questions) {

            interviewAnswerRepository.findByInterviewQuestion(question)
                    .ifPresent(answer -> interviewAnswerRepository.delete(answer));
        }

        // Delete Interview Questions
        interviewQuestionRepository.deleteByInterviewSession(session);

        // Delete Interview Evaluation
        interviewEvaluationRepository.findByInterviewSession(session)
                .ifPresent(evaluation -> interviewEvaluationRepository.delete(evaluation));

        // Delete Interview Session
        interviewSessionRepository.delete(session);
    }
}