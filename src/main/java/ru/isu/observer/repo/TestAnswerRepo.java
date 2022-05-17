package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.test.TestAnswer;

public interface TestAnswerRepo extends JpaRepository<TestAnswer, Long> {
}
