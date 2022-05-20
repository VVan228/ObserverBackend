package ru.isu.observer.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.Errors;

@Data
@AllArgsConstructor
public class EntityError {

    String field;
    String message;

}
