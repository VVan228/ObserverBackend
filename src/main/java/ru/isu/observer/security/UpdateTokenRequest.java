package ru.isu.observer.security;

import lombok.Data;

@Data
public class UpdateTokenRequest {

    String refresh_token;
}
