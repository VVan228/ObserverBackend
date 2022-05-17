package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.global.Subject;

import javax.persistence.Id;

public interface SubjectRepo extends JpaRepository<Subject, Long> {

}
