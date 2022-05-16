package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.hierarchy.Hierarchy;

public interface HierarchyRepo extends JpaRepository<Hierarchy, Long> {
}
