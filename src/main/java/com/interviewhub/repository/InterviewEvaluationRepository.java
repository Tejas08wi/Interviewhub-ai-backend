package com.interviewhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.interviewhub.entity.InterviewEvaluation;
import com.interviewhub.entity.InterviewSession;

@Repository
public interface InterviewEvaluationRepository extends JpaRepository<InterviewEvaluation, Long> {

    Optional<InterviewEvaluation> findByInterviewSession(InterviewSession interviewSession);

    @Query("SELECT AVG(i.overallScore) FROM InterviewEvaluation i")
    Double getAverageOverallScore();

    void deleteByInterviewSession(InterviewSession interviewSession);

}