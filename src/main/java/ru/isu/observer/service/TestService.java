package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.test.Question;
import ru.isu.observer.model.test.QuestionType;
import ru.isu.observer.model.test.Test;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.HierarchyRepo;
import ru.isu.observer.repo.TestRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    TestRepo testRepo;
    HierarchyService hierarchyService;
    UserService userService;


    @Autowired
    public TestService(TestRepo testRepo, HierarchyService hierarchyService, UserService userService) {
        this.testRepo = testRepo;
        this.hierarchyService = hierarchyService;
        this.userService = userService;
    }



    public Test createTest(Test test, List<Long> hierarchyIds){

        if(hierarchyIds==null || hierarchyIds.isEmpty()){
            hierarchyIds = new ArrayList<>(List.of(hierarchyService.getHierarchyIdByOrganisation(test.getCreator().getOrganisationId())));
        }

        boolean autoCheck = true;
        for(Question q: test.getQuestions()){
            if(q.getQuestionType() == QuestionType.OpenQuestionCheck){
                autoCheck = false;
            }
        }
        test.setAutoCheck(autoCheck);

        List<Hierarchy> nodes = new ArrayList<>();

        for(Long id: hierarchyIds){
            Hierarchy node = hierarchyService.getHierarchy(id);
            if(node !=null){
                nodes.add(node);
            }
        }

        test.setOpenedFor(hierarchyService.getStudentsWithAccess(nodes));

        for(Question q: test.getQuestions()){
            if(q.getQuestionType() == QuestionType.OpenQuestionCheck){
                test.setAutoCheck(false);
            }
        }

        testRepo.save(test);

        return test;

    }




    public Page<Test> getAutoCheckTestsPage(Long teacherId, Pageable pageable){
        return testRepo.getAutoCheckTestsPage(
                pageable,
                teacherId,
                false
        );
    }


    public Page<Test> getNotAutoCheckTestsPage(Long teacherId, Pageable pageable){
        return testRepo.getAutoCheckTestsPage(
                pageable,
                teacherId,
                true
        );
    }


    public Test getTest(Long testId){
        return testRepo.getById(testId);
    }

    public List<String> getQuestionTypes(){
        List<String> res = new ArrayList<>();
        for(QuestionType r: QuestionType.values()){
            res.add(r.name());
        }
        return res;
    }

    public Page<Test>getTestsWithNoStudentAnswer(Long studentId, Pageable pageable){
        return testRepo.getTestsWithNoStudentAnswer(pageable, studentId);
    }

    public Page<Test>getTestsWithStudentAnswer(Long studentId, Pageable pageable){
        return testRepo.getTestsWithNoStudentAnswer(pageable, studentId);
    }

}
