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

    @Convert(converter = ListToStringConverter.class)
    List<String> hierarchyLegend;


}
