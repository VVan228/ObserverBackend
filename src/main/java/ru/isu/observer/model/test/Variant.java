package ru.isu.observer.model.test;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Variant {
    String text;
}
