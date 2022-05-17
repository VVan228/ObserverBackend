package ru.isu.observer.model.hierarchy;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.isu.observer.model.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HierarchyLeaf extends Hierarchy{

    @Getter
    @Setter
    @OneToOne
    User student;
}
