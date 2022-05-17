package ru.isu.observer.service.test_validation_strategies;

import ru.isu.observer.model.test.Answer;
import ru.isu.observer.model.test.Question;
import ru.isu.observer.model.test.ScoredAnswer;

public class OpenQuestionValidation implements AnswerValidationStrategy{

    @Override
    public ScoredAnswer validate(Question q, Answer a) {
        String studentAnswer = a.getOpenAnswer();

        String rightAnswer = q.getRightAnswer().getOpenAnswer();

        ScoredAnswer res = new ScoredAnswer();

        boolean correct = studentAnswer.equals(rightAnswer);

        int score = 100 * Boolean.compare(correct, false) * q.getScoreScale();

        res.setAnswer(a);
        res.setScore(score);
        res.setQuestion(q);

        return res;
    }
}
