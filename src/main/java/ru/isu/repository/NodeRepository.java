package ru.isu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.model.hierarchy.Node;

public interface NodeRepository extends JpaRepository<Node, Long> {

}
