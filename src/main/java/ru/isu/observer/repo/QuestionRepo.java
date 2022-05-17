package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.test.Question;

public interface QuestionRepo extends JpaRepository<Question, Long> {
}
