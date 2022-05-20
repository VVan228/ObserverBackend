package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.isu.observer.service.UserService;

@Controller
public class UserController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

}
