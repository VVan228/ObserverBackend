package ru.isu.observer.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isu.observer.model.global.Organisation;
import ru.isu.observer.model.global.Subject;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "currentRefreshTokenHash", "status", "organisationId"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String password;
    String email;
    String name;
    String currentRefreshTokenHash;

    Role role;

    Status status;

    //@NotNull
    Long organisationId;

}
