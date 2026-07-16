package com.interviewhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByInterviewSession(InterviewSession interviewSession);

    Optional<InterviewQuestion> findByIdAndInterviewSession(
            Long id,
            InterviewSession interviewSession);

    void deleteByInterviewSession(InterviewSession interviewSession);

}