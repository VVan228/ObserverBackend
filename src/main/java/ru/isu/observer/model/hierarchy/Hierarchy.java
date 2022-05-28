package ru.isu.observer.model.hierarchy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Hierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@BatchSize(size=1)
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Hierarchy> children = new HashSet<>();

    public void addChild(Hierarchy child){
        children.add(child);
        child.setParentId(this.getId());
    }

    private Long parentId;
}
