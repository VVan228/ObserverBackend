package ru.isu.observer.model.test;

import lombok.Data;
import lombok.ToString;
import ru.isu.observer.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString
public class TestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Test test;

    @OneToMany(cascade = CascadeType.ALL)
    List<ScoredAnswer> answers = new ArrayList<>();

    boolean validated;

    @OneToOne
    User student;

    int totalScore;

}
