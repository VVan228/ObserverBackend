package ru.isu.observer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.global.Organisation;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.hierarchy.HierarchyBranch;
import ru.isu.observer.model.hierarchy.HierarchyLeaf;
import ru.isu.observer.model.hierarchy.HierarchyRoot;
import ru.isu.observer.model.test.*;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.repo.*;
import ru.isu.observer.service.*;

import javax.transaction.Transactional;
import java.util.*;

@Controller
public class HomeController {

    HierarchyService hierarchyService;
    SubjectService subjectService;
    OrganisationRepo organisationRepo;
    TestService testService;
    AnswerRepo answerRepo;
    QuestionRepo questionRepo;
    TestAnswerService testAnswerService;
    ScoredAnswerRepo scoredAnswerRepo;
    UserService userService;

    @Value("${pages.size}")
    private Integer PAGE_SIZE;

    boolean loaded = false;

    @Autowired
    public HomeController(HierarchyService hierarchyService,
                          SubjectService subjectService,
                          OrganisationRepo organisationRepo,
                          AnswerRepo answerRepo,
                          QuestionRepo questionRepo,
                          TestAnswerService testAnswerService,
                          ScoredAnswerRepo scoredAnswerRepo,
                          UserService userService,
                          TestService testService) {
        this.hierarchyService = hierarchyService;
        this.subjectService = subjectService;
        this.organisationRepo = organisationRepo;
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.testAnswerService = testAnswerService;
        this.scoredAnswerRepo = scoredAnswerRepo;
        this.userService = userService;
        this.testService = testService;


    }

    public void loadUsers(){
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword("user");
        user.setName("user");

        User user2 = new User();
        user2.setEmail("user2@mail.ru");
        user2.setPassword("user2");
        user2.setName("user2");

        User admin = new User();
        admin.setEmail("admin@mail.ru");
        admin.setPassword("admin");
        admin.setName("admin");
        admin.setRole(Role.ADMIN);

        User teacher = new User();
        teacher.setEmail("teacher@mail.ru");
        teacher.setPassword("teacher");
        teacher.setName("teacher");

        userService.saveStudent(user);
        userService.saveStudent(user2);
        userService.saveTeacher(teacher);
        userService.saveUser(admin);

        userService.updateUserName(user.getId(), "abobik");
    }

    public void loadHierarchy(){

        User student1 = userService.findUserByEmail("user@mail.ru");
        User student2 = userService.findUserByEmail("user2@mail.ru");
        Organisation org = organisationRepo.getByName("isu");

        HierarchyRoot root = new HierarchyRoot();
        HierarchyBranch branch = new HierarchyBranch();
        HierarchyBranch branch2 = new HierarchyBranch();
        HierarchyLeaf leaf = new HierarchyLeaf();
        HierarchyLeaf leaf2 = new HierarchyLeaf();

        root.setOrganisation(org);

        branch.setName("boba");
        branch2.setName("aboba2");

        leaf.setStudent(student1);
        leaf2.setStudent(student2);

        root.addChild(branch);
        root.addChild(branch2);

        branch.addChild(leaf);
        branch2.addChild(leaf2);

        hierarchyService.createHierarchy(root);
    }

    public void loadSubject(){
        User teacher = userService.findUserByEmail("teacher@mail.ru");
        Organisation org = organisationRepo.getByName("isu");

        Subject subject = new Subject();
        subject.setName("math");
        subject.setOrganisationId(org.getId());

        subjectService.addSubject(subject);
        subjectService.addTeacherToSubject(subject.getId(), teacher.getId());
        subjectService.setOrganisation(subject, org.getId());
    }

    public void loadOrganisation(){

        User admin = userService.findUserByEmail("admin@mail.ru");

        Organisation org = new Organisation();
        org.setAdministrator(admin);
        org.setHierarchyLegend(new ArrayList<>(List.of("one","two","three")));
        org.setName("isu");

        organisationRepo.save(org);

    }

    public void setOrganisationToUsers(){

        User student1 = userService.findUserByEmail("user@mail.ru");
        User student2 = userService.findUserByEmail("user2@mail.ru");
        User teacher = userService.findUserByEmail("teacher@mail.ru");
        User admin = userService.findUserByEmail("admin@mail.ru");
        Organisation org = organisationRepo.getByName("isu");


        userService.setOrganisation(student1.getId(), org.getId());
        userService.setOrganisation(student2.getId(), org.getId());
        userService.setOrganisation(admin.getId(), org.getId());
        userService.setOrganisation(teacher.getId(), org.getId());
    }

    public void loadTest(){
        User user1 = userService.getUser(1L);
        User user2 = userService.getUser(2L);
        User user3 = userService.getUser(3L);
        User user4 = userService.getUser(4L);

        Subject subj = subjectService.getSubject(1L);

        Hierarchy root = hierarchyService.getHierarchy(1L);

        Variant var1 = new Variant();
        var1.setText("var1 q1");
        var1.setId(1L);
        Variant var2 = new Variant();
        var2.setText("var2 q1");
        var2.setId(2L);

        Question q1 = new Question();
        Question q2 = new Question();
        q1.setVariants(List.of(var1, var2));
        //q2.setVariants(List.of(var3, var4));

        Answer a1 = new Answer();
        a1.setClosedAnswer(List.of(var1, var2));
        //Answer a2 = new Answer();
        //a2.setClosedAnswer(List.of(var4));

        q1.setRightAnswer(a1);
        q2.setRightAnswer(null);

        q1.setQuestionType(QuestionType.MulVarQuestion);
        q2.setQuestionType(QuestionType.OpenQuestionCheck);

        Test test = new Test();
        test.setTimeLimit(10L);
        test.setQuestions(List.of(q1,q2));
        test.setOpenedFor(Set.of(user1.getId(),user2.getId()));
        test.setAutoCheck(false);
        test.setSubject(subj);
        test.setCreator(user3);

        testService.createTest(test, List.of(root.getId()));
    }

    public void loadTestDataAnsw() {
        User user1 = userService.getUser(1L);
        User user2 = userService.getUser(2L);

        Test test = testService.getTest(1L);


        List<Question> questions = test.getQuestions();

        Question q1 = questions.get(0);
        Question q2 = questions.get(1);
        List<Variant> var1 = q1.getVariants();
        //List<Variant> var2 = q2.getVariants();

        Map<Long, Answer> answerMap = new HashMap<>();

        Answer a1 = new Answer();
        a1.setClosedAnswer(new ArrayList<>(List.of(var1.get(0), var1.get(1))));
        Answer a2 = new Answer();
        //a2.setClosedAnswer(List.of(var2.get(1)));
        a2.setOpenAnswer("abobik 228322");

        answerMap.put(q1.getId(), a1);
        answerMap.put(q2.getId(), a2);

        testAnswerService.createTestAnswer(test, user1, answerMap);

        testAnswerService.createTestAnswer(test, user1, answerMap);

        testAnswerService.createTestAnswer(test, user2, answerMap);

        ScoredAnswer sa = new ScoredAnswer();

        sa.setScore(15);
        sa.setQuestionId(q2.getId());
        sa.setAnswer(a2);
        sa.setComment("no u");

        testAnswerService.setCheckTestAnswer(1L, List.of(sa));
    }

    @ResponseBody
    @RequestMapping(value = "/{ID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Hierarchy main(
            @PathVariable Long ID,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Boolean> isAsc
            ) {

        if(!loaded){
            loadUsers();
            loadOrganisation();
            setOrganisationToUsers();

            loadSubject();
            loadHierarchy();
            loadTest();

            loadTestDataAnsw();


            loaded = true;
        }


        Boolean isAscB = isAsc.orElse(Boolean.TRUE);
        Sort.Direction dir = isAscB?Sort.Direction.ASC : Sort.Direction.DESC;


        //return subjectService.getSubjectsPage(ID, dir, page.orElse(0), sortBy.orElse("id"));
        /*
        * PageRequest.of(
                        page.orElse(0),
                        PAGE_SIZE,
                        dir,
                        sortBy.orElse("id")
                )*/


        return hierarchyService.getHierarchy(1L);
    }

}
