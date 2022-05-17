package ru.isu.observer.model.user;

import lombok.Getter;

@Getter
public enum Permission {

    HIERARCHY_WRITE("hierarchy:write"),
    HIERARCHY_READ("hierarchy:read");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
