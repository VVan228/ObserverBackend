package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.test.Test;
import ru.isu.observer.model.test.TestPlain;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.service.TestService;
import ru.isu.observer.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class TestController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    TestService testService;
    SmartValidator validator;



    @Autowired
    public TestController(TestService testService, SmartValidator validator) {
        this.testService = testService;
        this.validator = validator;
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
        validator.validate(testPlain, result);
        if(!result.hasErrors()){
            testService.createTest(testPlain.getTest(), testPlain.getAccess());
        }
        return EntityValidator.validate(result);
    }


    @ResponseBody
    @RequestMapping(
            value = "/tests/get/byUser",
            produces = "application/json"
    )
    public Page<Test> getTestByUser(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        //1L STUDENT
        //3L TEACHER
        //gonna take user from security context tho
        return testService.getTestsForUserPage(
                3L, Role.TEACHER,
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
            value = "/tests/get/testsToCheck",
            produces = "application/json"
    )
    public Page<Test> getTestsToCheck(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
    ){
        return testService.getNotAutoCheckTestsPage(
                3L,
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
