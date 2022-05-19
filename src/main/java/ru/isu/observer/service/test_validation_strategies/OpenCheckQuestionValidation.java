package ru.isu.observer.service.test_validation_strategies;

import ru.isu.observer.model.test.Answer;
import ru.isu.observer.model.test.Question;
import ru.isu.observer.model.test.ScoredAnswer;

public class OpenCheckQuestionValidation implements AnswerValidationStrategy{
    @Override
    public ScoredAnswer validate(Question q, Answer a) {


        String studentAnswer = a.getOpenAnswer();

        ScoredAnswer res = new ScoredAnswer();

        int score = 0;

        res.setAnswer(a);
        res.setScore(score);
        res.setQuestionId(q.getId());

        return res;
    }
}
