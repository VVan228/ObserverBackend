package ru.isu.observer.model.test;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String openAnswer;

    @ElementCollection
    List<Variant> closedAnswer = new ArrayList<>();
}
