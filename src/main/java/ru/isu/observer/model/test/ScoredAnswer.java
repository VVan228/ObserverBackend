package ru.isu.observer.model.test;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ScoredAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    Answer answer;

    @ManyToOne
    Question question;

    int score;
}
