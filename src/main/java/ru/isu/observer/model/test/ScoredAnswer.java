package ru.isu.observer.model.test;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
public class ScoredAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    Answer answer;

    Long questionId;

    int score;

    String comment;
}
