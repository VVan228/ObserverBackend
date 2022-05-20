package ru.isu.observer.model.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isu.observer.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be blank")
    String name;

    @OneToMany(cascade=CascadeType.ALL)
    Set<User> teachers = new HashSet<>();

    @NotNull(message = "organisationId must not be null")
    Long organisationId;

    public void addTeacher(User user){
        teachers.add(user);
    }
}
