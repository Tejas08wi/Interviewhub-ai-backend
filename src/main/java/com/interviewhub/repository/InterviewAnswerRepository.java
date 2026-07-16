package com.interviewhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interviewhub.entity.InterviewAnswer;
import com.interviewhub.entity.InterviewQuestion;

@Repository
public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    Optional<InterviewAnswer> findByInterviewQuestion(InterviewQuestion interviewQuestion);

    void deleteByInterviewQuestion(InterviewQuestion interviewQuestion);

}