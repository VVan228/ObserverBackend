package ru.isu.observer.model.test;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@ToString
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ElementCollection
    List<Variant> variants;

    @OneToOne(cascade = CascadeType.ALL)
    Answer rightAnswer;

    QuestionType questionType;

    int scoreScale = 1;

    String questionText;

}
