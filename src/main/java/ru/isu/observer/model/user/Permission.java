package ru.isu.observer.model.user;

import lombok.Getter;

@Getter
public enum Permission {

    HIERARCHY_WRITE("hierarchy:write"),
    HIERARCHY_READ("hierarchy:read"),
    USERS_CREATE("users:create"),
    USERS_GET("users:get"),
    TESTS_CREATE("tests:create"),
    TESTS_EDIT("tests:edit"),
    TESTS_GET("tests:get"),
    TEST_ANSWERS_GET("testAnswers:get"),
    TEST_ANSWERS_CREATE("testAnswers:create"),
    TEST_ANSWERS_EDIT("testAnswers:edit"),
    SUBJECTS_CREATE("subjects:create"),
    SUBJECTS_EDIT("subjects:edit"),
    SUBJECTS_GET("subjects:get");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
