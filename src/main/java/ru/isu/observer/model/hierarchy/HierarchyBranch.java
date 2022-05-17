package ru.isu.observer.model.hierarchy;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
public class HierarchyBranch extends Hierarchy{

    @Getter
    @Setter
    String name;
}
