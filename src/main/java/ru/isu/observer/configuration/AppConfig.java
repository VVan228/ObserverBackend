package ru.isu.observer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.isu.observer.service.test_validation_strategies.*;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.isu.observer")
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST");
    }

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
