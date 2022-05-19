package ru.isu.observer.service.test_validation_strategies;

import ru.isu.observer.model.test.*;

import java.util.List;

public class OneVarQuestionValidation implements AnswerValidationStrategy{

    @Override
    public ScoredAnswer validate(Question q, Answer a) {
        List<Variant> rightAnswerVariants = q.getRightAnswer().getClosedAnswer();
        List<Variant> studentAnswerVariants = a.getClosedAnswer();
        
        boolean fitsSize = rightAnswerVariants.size()==1 && studentAnswerVariants.size()==1;
        boolean right = false;
        
        if(fitsSize){
            right = rightAnswerVariants.containsAll(studentAnswerVariants);
        }
        int score = Boolean.compare(right, false) * (q.getScoreScale() * 100);
        
        ScoredAnswer res = new ScoredAnswer();

        res.setAnswer(a);
        res.setScore(score);
        res.setQuestionId(q.getId());
        
        return res;
    }
}
