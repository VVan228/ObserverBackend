package ru.isu.observer.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isu.observer.model.global.Organisation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String password;
    String email;
    String name;
    String currentRefreshTokenHash;

    Role role;

    @NotNull
    Long organisationId;

}
