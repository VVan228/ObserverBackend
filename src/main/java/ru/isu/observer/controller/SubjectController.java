package ru.isu.observer.controller;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.global.SubjectPlain;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.security.SecurityUser;
import ru.isu.observer.service.SubjectService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SubjectController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @ResponseBody
    @RequestMapping(value="/subjects/get/{ID}", produces = "application/json")
    public Subject getOneSubject(@PathVariable Long ID){
        return subjectService.getSubject(ID);
    }

    @ResponseBody
    @RequestMapping(value="/subjects/get/all", produces = "application/json")
    public List<SubjectPlain> getAllSubjects(){
        SecurityUser ud = SecurityUser.getCurrent();
        return subjectService.getSubjects(ud.getUser().getOrganisationId());
    }

    @ResponseBody
    @RequestMapping(
            value="/subjects/save",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public ResponseEntity<List<EntityError>> saveSubject(
            @Valid @RequestBody Subject subject,
            Errors errors
    ){
        if(!errors.hasErrors()){
            subjectService.addSubject(subject);
        }
        return EntityValidator.validate(errors);
    }

    @ResponseBody
    @RequestMapping(
            value = "/subjects/update/name/{id}/{name}",
            method = RequestMethod.POST
    )
    public ResponseEntity<String> updateSubjectName(@PathVariable String name, @PathVariable Long id){
        subjectService.updateSubjectName(id, name);
        return ResponseEntity.ok().body(null);
    }


    @ResponseBody
    @RequestMapping(
            value = "/subjects/update/addTeacher/{subjectId}/{userId}",
            method = RequestMethod.POST
    )
    public ResponseEntity<String> addTeacherToSubject(@PathVariable Long subjectId, @PathVariable Long userId){
        subjectService.addTeacherToSubject(subjectId, userId);
        return ResponseEntity.ok().body(null);
    }




}