package ru.isu.observer.service.test_validation_strategies;

import ru.isu.observer.model.test.Answer;
import ru.isu.observer.model.test.Question;
import ru.isu.observer.model.test.ScoredAnswer;
import ru.isu.observer.model.test.Variant;

import java.util.List;

public class OrderQuestionValidation implements AnswerValidationStrategy{

    @Override
    public ScoredAnswer validate(Question q, Answer a) {
        List<Variant> rightAnswerVariants = q.getRightAnswer().getClosedAnswer();
        List<Variant> studentAnswerVariants = a.getClosedAnswer();

        int amount = 0;

        for(int i = 0; i<studentAnswerVariants.size(); i++){
            if(studentAnswerVariants.get(i) == rightAnswerVariants.get(i)){
                amount++;
            }
        }

        int score = (100 * amount * q.getScoreScale())/rightAnswerVariants.size();

        ScoredAnswer res = new ScoredAnswer();

        res.setAnswer(a);
        res.setScore(score);
        res.setQuestion(q);

        return res;
    }
}
