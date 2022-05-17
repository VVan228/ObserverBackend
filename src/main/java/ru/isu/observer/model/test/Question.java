package ru.isu.observer.model.test;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ElementCollection
    List<Variant> variants;

    @OneToOne(cascade = CascadeType.ALL)
    Answer rightAnswer;

    int scoreScale;

}
