package ru.isu.observer.model.hierarchy;

import ru.isu.observer.model.global.Organisation;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HierarchyRoot extends Hierarchy{

    @OneToOne
    Organisation organisation;
}
