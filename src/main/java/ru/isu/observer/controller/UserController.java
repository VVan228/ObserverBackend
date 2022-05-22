package ru.isu.observer.controller;

import com.github.javafaker.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.user.Status;
import ru.isu.observer.model.user.User;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.security.SecurityUser;
import ru.isu.observer.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    Sort.Direction getDir(Optional<Boolean> isAsc){
        Boolean isAscB = isAsc.orElse(Boolean.TRUE);
        return isAscB?Sort.Direction.ASC : Sort.Direction.DESC;
    }

    @ResponseBody
    @RequestMapping(
            value = "/users/save/student",
            consumes = "application/json",
            method = RequestMethod.POST
    )
    public ResponseEntity<List<EntityError>> saveStudent(
            @Valid @RequestBody User user,
            Errors errors
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        if(!errors.hasErrors()){
            user.setOrganisationId(ud.getUser().getOrganisationId());
            userService.saveStudent(user);
        }
        return EntityValidator.validate(errors);
    }

    @ResponseBody
    @RequestMapping(
            value = "/users/save/teacher",
            consumes = "application/json",
            method = RequestMethod.POST
    )
    public ResponseEntity<List<EntityError>> saveTeacher(
            @Valid @RequestBody User user,
            Errors errors
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        if(!errors.hasErrors()){
            user.setOrganisationId(ud.getUser().getOrganisationId());
            userService.saveTeacher(user);
        }
        return EntityValidator.validate(errors);
    }

    @ResponseBody
    @RequestMapping(
            value = "/users/get/students",
            produces = "application/json",
            method = RequestMethod.GET
    )
    public Page<User> getStudents(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        return userService.getStudentsPage(ud.getUser().getOrganisationId(),
                PageRequest.of(
                        page.orElse(0),
                        PAGE_SIZE,
                        getDir(isAsc),
                        sortBy.orElse("id")
                )
        );
    }

    @ResponseBody
    @RequestMapping(
            value = "/users/get/teachers",
            produces = "application/json",
            method = RequestMethod.GET
    )
    public Page<User> getTeachers(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        return userService.getTeachersPage(ud.getUser().getOrganisationId(),
                PageRequest.of(
                        page.orElse(0),
                        PAGE_SIZE,
                        getDir(isAsc),
                        sortBy.orElse("id")
                )
        );
    }


    @ResponseBody
    @RequestMapping(
            value = "/users/update/name/{name}",
            method = RequestMethod.POST
    )
    public void updateName(@PathVariable String name){
        SecurityUser ud = SecurityUser.getCurrent();
        userService.updateUserName(ud.getUser().getId(), name);
    }


    @ResponseBody
    @RequestMapping(
            value = "/users/update/email/{email}",
            method = RequestMethod.POST
    )
    public void updateEmail(@PathVariable String email){
        SecurityUser ud = SecurityUser.getCurrent();
        userService.updateUserEmail(ud.getUser().getId(), email);
    }

    @ResponseBody
    @RequestMapping(
            value = "/users/get/{id}",
            produces = "application/json"
    )
    public User getUser(@PathVariable Long id){
        return userService.getUser(id);
    }


    //private SecurityUser getCurrentSecurityUser(){
    //    return  ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    //}
}
