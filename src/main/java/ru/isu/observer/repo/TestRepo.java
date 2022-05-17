package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isu.observer.model.test.Test;

public interface TestRepo extends JpaRepository<Test, Long> {
}
