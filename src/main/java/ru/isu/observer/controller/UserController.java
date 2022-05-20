package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.user.User;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(
            value = "/users/save/student",
            consumes = "application/json",
            method = RequestMethod.POST
    )
    public ResponseEntity<List<EntityError>> saveStudent(
            @Valid @RequestBody User user,
            Errors errors
    ){
        if(!errors.hasErrors()){
            userService.saveStudent(user);
        }
        return EntityValidator.validate(errors);
    }

}
