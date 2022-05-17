package ru.isu.observer.model.hierarchy;

import ru.isu.observer.model.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HierarchyLeaf extends Hierarchy{

    @OneToOne
    User student;
}
