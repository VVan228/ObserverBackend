package ru.isu.observer.model.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Role {
    STUDENT(Set.of(
            Permission.TESTS_GET,
            Permission.TEST_ANSWERS_GET,
            Permission.TEST_ANSWERS_CREATE
    )),
    TEACHER(Set.of(
            Permission.TESTS_CREATE,
            Permission.TESTS_EDIT,
            Permission.TESTS_GET,
            Permission.HIERARCHY_READ,
            Permission.TEST_ANSWERS_EDIT,
            Permission.TEST_ANSWERS_GET
    )),
    ADMIN(Set.of(
            Permission.HIERARCHY_WRITE,
            Permission.HIERARCHY_READ,
            Permission.USERS_CREATE,
            Permission.USERS_GET,
            Permission.SUBJECTS_CREATE,
            Permission.SUBJECTS_EDIT,
            Permission.SUBJECTS_GET
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
