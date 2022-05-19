package ru.isu.observer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.isu.observer.service.test_validation_strategies.*;

@Configuration
public class AppConfig {

    @Bean
    public AnswerValidationStrategy oneVarStrategy(){
        return new OneVarQuestionValidation();
    }

    @Bean
    public AnswerValidationStrategy mulVarStrategy(){
        return new MulVarQuestionValidation();
    }

    @Bean
    public AnswerValidationStrategy openStrategy(){
        return new OpenQuestionValidation();
    }

    @Bean
    public AnswerValidationStrategy orderStrategy(){
        return new OrderQuestionValidation();
    }

    @Bean
    public AnswerValidationStrategy openCheckStrategy(){
        return new OpenCheckQuestionValidation();
    }
}
