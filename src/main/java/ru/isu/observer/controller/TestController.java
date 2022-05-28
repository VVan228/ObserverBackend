package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.test.Test;
import ru.isu.observer.model.test.TestPlain;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.security.SecurityUser;
import ru.isu.observer.service.HierarchyService;
import ru.isu.observer.service.TestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class TestController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    TestService testService;
    HierarchyService hierarchyService;
    SmartValidator validator;



    @Autowired
    public TestController(TestService testService, SmartValidator validator, HierarchyService hierarchyService) {
        this.testService = testService;
        this.validator = validator;
        this.hierarchyService = hierarchyService;
    }
    Sort.Direction getDir(Optional<Boolean> isAsc){
        Boolean isAscB = isAsc.orElse(Boolean.TRUE);
        return isAscB?Sort.Direction.ASC : Sort.Direction.DESC;
    }

    @ResponseBody
    @RequestMapping(
            value = "/tests/save",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public ResponseEntity<List<EntityError>> saveTest(
            @RequestBody TestPlain testPlain,
            BindingResult result
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        testPlain.getTest().setCreator(ud.getUser());
        validator.validate(testPlain, result);
        if(!result.hasErrors()){

            testService.createTest(testPlain.getTest(), testPlain.getAccess());
        }
        return EntityValidator.validate(result);
    }


    @ResponseBody
    @RequestMapping(
            value = "/tests/get/byUser/notToCheck",
            produces = "application/json"
    )
    public Page<Test> getTestsNotToCheck(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        return testService.getAutoCheckTestsPage(
                ud.getUser().getId(),
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
            value = "/tests/get/byUser",
            produces = "application/json"
    )
    public Page<Test> getTestsByUser(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        if(ud.getUser().getRole() == Role.STUDENT){
            return testService.getTestsWithStudentAnswer(
                    ud.getUser().getId(),
                    PageRequest.of(
                            page.orElse(0),
                            PAGE_SIZE,
                            getDir(isAsc),
                            sortBy.orElse("id")
                    )
            );
        }else if(ud.getUser().getRole() == Role.TEACHER){
            return testService.getAutoCheckTestsPage(
                    ud.getUser().getId(),
                    PageRequest.of(
                            page.orElse(0),
                            PAGE_SIZE,
                            getDir(isAsc),
                            sortBy.orElse("id")
                    )
            );
        }else{
            return null;
        }

    }

    @ResponseBody
    @RequestMapping(
            value = "/tests/get/byUser/toCheck",
            produces = "application/json"
    )
    public Page<Test> getTestsToCheck(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        return testService.getNotAutoCheckTestsPage(
                ud.getUser().getId(),
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
            value = "/tests/get/byUser/notAnswered",
            produces = "application/json"
    )
    public Page<Test> getNotAnsweredTests(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        return testService.getTestsWithNoStudentAnswer(ud.getUser().getId(),PageRequest.of(
                page.orElse(0),
                PAGE_SIZE,
                getDir(isAsc),
                sortBy.orElse("id")
        ));
    }

    @ResponseBody
    @RequestMapping(
            value = "/tests/get/byUser/answered",
            produces = "application/json"
    )
    public Page<Test> getAnsweredTests(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        return testService.getTestsWithStudentAnswer(ud.getUser().getId(),PageRequest.of(
                page.orElse(0),
                PAGE_SIZE,
                getDir(isAsc),
                sortBy.orElse("id")
        ));
    }

    @ResponseBody
    @RequestMapping(
            value = "/tests/get/{id}",
            produces = "application/json"
    )
    public Test getTest(@PathVariable Long id){
        return testService.getTest(id);
    }

    @ResponseBody
    @RequestMapping(
            value = "/tests/get/questionTypes",
            produces = "application/json"
    )
    public List<String> getQuestionTypes(){
        return testService.getQuestionTypes();
    }

}
