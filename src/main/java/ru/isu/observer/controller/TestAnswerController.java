package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.hierarchy.HierarchyRoot;
import ru.isu.observer.model.test.ScoredAnswer;
import ru.isu.observer.model.test.TestAnswer;
import ru.isu.observer.model.test.TestAnswerPlain;
import ru.isu.observer.repo.TestAnswerRepo;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.security.SecurityUser;
import ru.isu.observer.service.TestAnswerService;
import ru.isu.observer.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class TestAnswerController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    TestAnswerService testAnswerService;


    @Autowired
    public TestAnswerController(TestAnswerService testAnswerService) {
        this.testAnswerService = testAnswerService;
    }
    Sort.Direction getDir(Optional<Boolean> isAsc){
        Boolean isAscB = isAsc.orElse(Boolean.TRUE);
        return isAscB?Sort.Direction.ASC : Sort.Direction.DESC;
    }


    @ResponseBody
    @RequestMapping(
            value = "/testAnswers/get/byTest/{testId}"
    )
    public Page<TestAnswer> getByTest(
            @PathVariable Long testId,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc,
            @RequestParam Optional<Boolean> validated
    ){
        boolean getValidatedOnly = validated.orElse(Boolean.FALSE);
        if(getValidatedOnly){
            return testAnswerService.getValidatedTestAnswersPage(
                    PageRequest.of(
                            page.orElse(0),
                            PAGE_SIZE,
                            getDir(isAsc),
                            sortBy.orElse("id")
                    ),
                    testId);
        }else{
            return testAnswerService.getNotValidatedTestAnswersPage(
                    PageRequest.of(
                            page.orElse(0),
                            PAGE_SIZE,
                            getDir(isAsc),
                            sortBy.orElse("id")
                    ),
                    testId);
        }
    }


    @ResponseBody
    @RequestMapping(
            value = "/testAnswers/get/{answerId}"
    )
    public TestAnswer getTestAnswer(@PathVariable Long answerId){
        return testAnswerService.getTestAnswer(answerId);
    }

    @ResponseBody
    @RequestMapping(
            value = "/testAnswers/get/byStudent"
    )
    public Page<TestAnswer> getByStudent(
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc,
            @RequestParam Optional<Boolean> validated
    ){
        boolean getValidatedOnly = validated.orElse(Boolean.TRUE);
        SecurityUser ud = SecurityUser.getCurrent();
        Long userId = ud.getUser().getId();
        if(getValidatedOnly){
            return testAnswerService.getStudentTestAnswersValidatedPage(
                    PageRequest.of(
                            page.orElse(0),
                            PAGE_SIZE,
                            getDir(isAsc),
                            sortBy.orElse("id")
                    ),
                    userId);
        }else{
            return testAnswerService.getStudentTestAnswersNotValidatedPage(
                    PageRequest.of(
                            page.orElse(0),
                            PAGE_SIZE,
                            getDir(isAsc),
                            sortBy.orElse("id")
                    ),
                    userId);
        }
    }


    @ResponseBody
    @RequestMapping(
            value="/testAnswers/save",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public ResponseEntity<List<EntityError>> saveTestAnswer(
            @Valid @RequestBody TestAnswerPlain testAnswerPlain,
            BindingResult result
    ){
        SecurityUser ud = SecurityUser.getCurrent();
        if(!result.hasErrors()){
            testAnswerService.createTestAnswer(
                    testAnswerPlain.getTestId(),
                    ud.getUser(),
                    testAnswerPlain.getAnswers()
            );
        }
        return EntityValidator.validate(result);
    }


    @ResponseBody
    @RequestMapping(
            value="/testAnswers/set/checkAnswer/{testId}",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public ResponseEntity<List<EntityError>> setCheckAnswer(
            @Valid @RequestBody List<ScoredAnswer> answers,
            @PathVariable Long testId,
            BindingResult result
    ){
        if(!result.hasErrors()){
            testAnswerService.setCheckTestAnswer(testId, answers);
        }
        return EntityValidator.validate(result);
    }

    @ResponseBody
    @RequestMapping(
            value="/testAnswers/set/totalScore/{testAnswerId}/{score}",
            method = RequestMethod.POST
    )
    public void setTotalScore(
            @PathVariable Long testAnswerId,
            @PathVariable Integer score
    ){
        testAnswerService.setTotalScore(testAnswerId, score);
    }

}
