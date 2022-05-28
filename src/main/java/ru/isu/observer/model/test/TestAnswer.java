package ru.isu.observer.model.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ru.isu.observer.model.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long testId;

    @OneToMany(cascade = CascadeType.ALL)
    List<ScoredAnswer> answers = new ArrayList<>();

    boolean validated;

    @OneToOne
    User student;

    int totalScore;

    int maxScore;


    long dateMillis;

}
