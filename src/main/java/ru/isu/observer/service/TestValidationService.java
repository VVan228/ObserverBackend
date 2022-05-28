package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.test.*;
import ru.isu.observer.service.test_validation_strategies.AnswerValidationStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestValidationService {

    AnswerValidationStrategy oneVarVal;
    AnswerValidationStrategy mulVarVal;
    AnswerValidationStrategy openVal;
    AnswerValidationStrategy orderVal;
    AnswerValidationStrategy openCheckVal;

    TestValidationService(
            @Qualifier("oneVarStrategy") AnswerValidationStrategy oneVarVal,
            @Qualifier("mulVarStrategy") AnswerValidationStrategy mulVarVal,
            @Qualifier("openStrategy") AnswerValidationStrategy openVal,
            @Qualifier("orderStrategy") AnswerValidationStrategy orderVal,
            @Qualifier("openCheckStrategy") AnswerValidationStrategy openCheckVal
    ){
        this.oneVarVal = oneVarVal;
        this.mulVarVal = mulVarVal;
        this.openVal = openVal;
        this.orderVal = orderVal;
        this.openCheckVal = openCheckVal;
    }


    public TestAnswer validateTest(Test test, Map<Long, Answer> answers){

        TestAnswer res = new TestAnswer();
        res.setTestId(test.getId());
        res.setValidated(true);

        Map<Long, Question> testQuestions = new HashMap<>();
        List<ScoredAnswer> studentAnswers = new ArrayList<>();

        for(Question q: test.getQuestions()){
            testQuestions.put(q.getId(), q);
        }

        int totalScore = 0;
        int maxScore = 0;

        for (Map.Entry<Long, Answer> answer : answers.entrySet()) {
            Question q = testQuestions.get(answer.getKey());
            maxScore += q.getScoreScale()*100;

            ScoredAnswer sa = new ScoredAnswer();

            switch (q.getQuestionType()){
                case OneVarQuestion -> sa = oneVarVal.validate(q, answer.getValue());
                case MulVarQuestion -> sa = mulVarVal.validate(q, answer.getValue());
                case OpenQuestion -> sa = openVal.validate(q, answer.getValue());
                case OrderQuestion -> sa = orderVal.validate(q, answer.getValue());
                case OpenQuestionCheck -> {
                    sa = openCheckVal.validate(q, answer.getValue());
                    res.setValidated(false);
                }
            }

            studentAnswers.add(sa);
            totalScore += sa.getScore();

        }
        res.setMaxScore(maxScore);
        res.setAnswers(studentAnswers);
        res.setTotalScore(totalScore);

        return res;
    }

    public int getTotalScore(TestAnswer testAnswer){
        int totalScore = 0;
        for(ScoredAnswer sa: testAnswer.getAnswers()){
            totalScore += sa.getScore();
        }
        return totalScore;
    }

}
