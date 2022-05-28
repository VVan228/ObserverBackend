package ru.isu.observer.model.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Test {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Long timeLimit;

    @BatchSize(size=3)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Question> questions = new ArrayList<>();

    //@ManyToMany
    @ElementCollection
    Set<Long> openedFor = new HashSet<>();

    boolean autoCheck;

    Long subjectId;

    @ManyToOne
    User creator;

    String name;
}
