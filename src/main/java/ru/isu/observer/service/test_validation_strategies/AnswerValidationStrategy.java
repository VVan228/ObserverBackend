package ru.isu.observer.service.test_validation_strategies;

import ru.isu.observer.model.test.Answer;
import ru.isu.observer.model.test.Question;
import ru.isu.observer.model.test.ScoredAnswer;

public interface AnswerValidationStrategy {
    ScoredAnswer validate(Question q, Answer a);
}
