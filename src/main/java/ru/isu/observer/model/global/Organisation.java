package ru.isu.observer.model.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.isu.observer.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    @OneToOne(cascade=CascadeType.ALL)
    User administrator;

    String hierarchyLegend;

    public List<String> getHierarchyLegend() {
        return List.of(hierarchyLegend.split(","));
    }

    public void setHierarchyLegend(List<String> hierarchyLegend) {
        StringBuilder sb = new StringBuilder();
        hierarchyLegend.forEach((el)-> sb.append(el).append(","));
        if(!sb.isEmpty()){
            sb.deleteCharAt(sb.length()-1);
        }
        this.hierarchyLegend = sb.toString();
    }

}
