package ru.isu.observer.model.hierarchy;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.isu.observer.model.global.Organisation;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HierarchyRoot extends Hierarchy{

    @Getter
    @Setter
    @OneToOne
    Organisation organisation;
}
