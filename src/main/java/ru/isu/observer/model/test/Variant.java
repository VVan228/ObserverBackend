package ru.isu.observer.model.test;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Variant {
    String text;
    Long id;

    @Override
    public String toString() {
        return text + " " + id;
    }
}
