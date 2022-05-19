package ru.isu.observer.service.test_validation_strategies;

import ru.isu.observer.model.test.Answer;
import ru.isu.observer.model.test.Question;
import ru.isu.observer.model.test.ScoredAnswer;
import ru.isu.observer.model.test.Variant;

import java.util.List;

public class MulVarQuestionValidation implements AnswerValidationStrategy{

    @Override
    public ScoredAnswer validate(Question q, Answer a) {

        List<Variant> rightAnswerVariants = q.getRightAnswer().getClosedAnswer();
        List<Variant> studentAnswerVariants = a.getClosedAnswer();

        int amount = 0;

        for(Variant v: studentAnswerVariants){
            if(rightAnswerVariants.contains(v)){
                amount++;
            }else{
                amount--;
            }
        }

        amount = Math.max(amount, 0);

        int score = (100 * amount * q.getScoreScale())/rightAnswerVariants.size();

        ScoredAnswer res = new ScoredAnswer();

        res.setAnswer(a);
        res.setScore(score);
        res.setQuestionId(q.getId());

        return res;
    }
}
