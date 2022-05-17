package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.test.ScoredAnswer;

public interface ScoredAnswerRepo extends JpaRepository<ScoredAnswer, Long> {
}
