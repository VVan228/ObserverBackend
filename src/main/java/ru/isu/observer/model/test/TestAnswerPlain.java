package ru.isu.observer.model.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestAnswerPlain {

    @NotNull
    Map<Long, Answer> answers;

    @NotNull
    Long testId;

}
