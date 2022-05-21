package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.test.Answer;
import ru.isu.observer.model.test.ScoredAnswer;
import ru.isu.observer.model.test.Test;
import ru.isu.observer.model.test.TestAnswer;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.TestAnswerRepo;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestAnswerService {

    TestAnswerRepo testAnswerRepo;
    TestValidationService testValidationService;
    TestService testService;

    @Autowired
    public TestAnswerService(TestAnswerRepo testAnswerRepo, TestValidationService testValidationService, TestService testService) {
        this.testAnswerRepo = testAnswerRepo;
        this.testValidationService = testValidationService;
        this.testService = testService;
    }

    public List<TestAnswer> getTestAnswers(Long testId){
        return testAnswerRepo.getTestAnswersByTestId(testId);
    }
    public Page<TestAnswer> getTestAnswersPage(Pageable pageable, Long testId){
        return testAnswerRepo.getTestAnswersByTestId(pageable, testId);
    }
    public List<TestAnswer> getTestAnswers(Test test){
        return getTestAnswers(test.getId());
    }

    public TestAnswer createTestAnswer(Long testId, User student, Map<Long, Answer> answers){
        Test t = testService.getTest(testId);
        return createTestAnswer(t, student, answers);
    }
    public TestAnswer createTestAnswer(Test test, User student, Map<Long, Answer> answers){
        TestAnswer res = testValidationService.validateTest(test, answers);
        res.setStudent(student);
        testAnswerRepo.save(res);
        return res;
    }

    public TestAnswer getTestAnswer(Long id){
        return testAnswerRepo.getById(id);
    }

    @Transactional
    public void setTotalScore(Long testAnswerId, int score){
        testAnswerRepo.updateTotalScore(testAnswerId, score);
    }

    public TestAnswer setTotalScore(TestAnswer testAnswer, int score){
        testAnswer.setTotalScore(score);
        testAnswerRepo.save(testAnswer);
        return testAnswer;
    }

    public TestAnswer setCheckTestAnswer(TestAnswer testAnswer, List<ScoredAnswer> scoredAnswers){

        Map<Long, ScoredAnswer> studentTestAnswers = new HashMap<>();

        for(ScoredAnswer sa: testAnswer.getAnswers()){
            studentTestAnswers.put(sa.getQuestionId(), sa);
        }

        for(ScoredAnswer sa: scoredAnswers){
            if(!studentTestAnswers.containsKey(sa.getQuestionId())){
                throw new EntityNotFoundException("no such question '" + sa.getQuestionId() + "'");
            }
            studentTestAnswers.get(sa.getQuestionId()).setScore(sa.getScore());
            studentTestAnswers.get(sa.getQuestionId()).setComment(sa.getComment());
            Answer answer = studentTestAnswers.get(sa.getQuestionId()).getAnswer();
            studentTestAnswers.get(sa.getQuestionId()).setAnswer(answer);
        }


        testAnswer.setTotalScore(testValidationService.getTotalScore(testAnswer));

        testAnswer.setValidated(true);
        testAnswerRepo.save(testAnswer);
        return testAnswer;
    }
    public TestAnswer setCheckTestAnswer(Long testAnswerId, List<ScoredAnswer> scoredAnswers){
        TestAnswer ta = testAnswerRepo.getById(testAnswerId);
        return setCheckTestAnswer(ta, scoredAnswers);
    }

    public List<TestAnswer> getStudentTestAnswers(Long userId){
        return testAnswerRepo.getStudentTestAnswers(userId);
    }
    public List<TestAnswer> getStudentTestAnswers(User user){
        return testAnswerRepo.getStudentTestAnswers(user.getId());
    }

    public Page<TestAnswer> getStudentTestAnswersPage(Pageable pageable, Long userId){
        return testAnswerRepo.getStudentTestAnswers(pageable, userId);
    }
    public Page<TestAnswer> getStudentTestAnswersPage(Pageable pageable, User user){
        return testAnswerRepo.getStudentTestAnswers(pageable, user.getId());
    }

    public List<TestAnswer> getStudentTestAnswersValidated(Long userId){
        return testAnswerRepo.getStudentTestAnswersValidated(userId);
    }
    public List<TestAnswer> getStudentTestAnswersValidated(User user){
        return testAnswerRepo.getStudentTestAnswersValidated(user.getId());
    }

    public Page<TestAnswer> getStudentTestAnswersValidatedPage(Pageable pageable, Long userId){
        return testAnswerRepo.getStudentTestAnswersValidated(pageable, userId);
    }
    public Page<TestAnswer> getStudentTestAnswersValidatedPage(Pageable pageable, User user){
        return testAnswerRepo.getStudentTestAnswersValidated(pageable, user.getId());
    }

}
