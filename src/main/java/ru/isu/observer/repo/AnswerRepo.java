package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.test.Answer;

public interface AnswerRepo extends JpaRepository<Answer, Long> {
}
