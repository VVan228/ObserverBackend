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

    public List<Test> getTestsForUser(Long userId, Role role){
        if(role == Role.TEACHER){
            return testRepo.getTestsForTeacher(userId);
        }else if(role == Role.STUDENT){
            return testRepo.getTestsForStudent(userId);
        }
        return null;
    }
    public List<Test> getTestsForUser(User user){
        return getTestsForUser(user.getId(), user.getRole());
    }
    public List<Test> getTestsForUser(Long userId){
        return getTestsForUser(userService.getUser(userId));
    }
    public List<Test> getNotAutoCheckTests(Long teacherId){
        return testRepo.getNotAutoCheckTests(teacherId);
    }
    public List<Test> getNotAutoCheckTests(User teacher){
        return testRepo.getNotAutoCheckTests(teacher.getId());
    }



    public Page<Test> getTestsForUserPage(User user, Pageable pageable){
        return getTestsForUserPage(user.getId(), user.getRole(), pageable);
    }
    public Page<Test> getTestsForUserPage(Long userId, Role role, Pageable pageable) {
        if(role == Role.TEACHER){
            return testRepo.getTestsForTeacherPage(
                    pageable,
                    userId
            );
        }else if(role == Role.STUDENT){
            return testRepo.getTestsForStudentPage(
                    pageable,
                    userId
            );
        }
        return null;
    }
    public Page<Test> getNotAutoCheckTestsPage(Long teacherId, Pageable pageable){
        return testRepo.getNotAutoCheckTestsPage(
                pageable,
                teacherId
        );
    }
    public Page<Test> getNotAutoCheckTestsPage(User teacher, Pageable pageable){
        return testRepo.getNotAutoCheckTestsPage(
                pageable,
                teacher.getId()
        );
    }


    public Test getTest(Long testId){
        return testRepo.getById(testId);
    }

    public List<String> getQuestionTypes(){
        List<String> res = new ArrayList<>();
        for(Role r: Role.values()){
            res.add(r.name());
        }
        return res;
    }

}
