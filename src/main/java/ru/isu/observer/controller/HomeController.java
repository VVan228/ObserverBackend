package ru.isu.observer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import ru.isu.observer.service.HierarchyService;
import ru.isu.observer.service.SubjectService;
import ru.isu.observer.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    HierarchyService hierarchyService;
    SubjectService subjectService;
    OrganisationRepo organisationRepo;
    TestRepo testRepo;
    AnswerRepo answerRepo;
    QuestionRepo questionRepo;
    TestAnswerRepo testAnswerRepo;
    ScoredAnswerRepo scoredAnswerRepo;
    UserService userService;

    boolean loaded = false;

    @Autowired
    public HomeController(HierarchyService hierarchyService,
                          SubjectService subjectService,
                          OrganisationRepo organisationRepo,
                          TestRepo testRepo,
                          AnswerRepo answerRepo,
                          QuestionRepo questionRepo,
                          TestAnswerRepo testAnswerRepo,
                          ScoredAnswerRepo scoredAnswerRepo,
                          UserService userService) {
        this.hierarchyService = hierarchyService;
        this.subjectService = subjectService;
        this.organisationRepo = organisationRepo;
        this.testRepo = testRepo;
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.testAnswerRepo = testAnswerRepo;
        this.scoredAnswerRepo = scoredAnswerRepo;
        this.userService = userService;


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

        subjectService.addSubject(subject);
        subjectService.addTeacherToSubject(subject.getId(), teacher.getId());
        subjectService.setOrganisation(subject, org.getId());
    }

    public void loadOrganisation(){

        User admin = userService.findUserByEmail("admin@mail.ru");

        Organisation org = new Organisation();
        org.setAdministrator(admin);
        org.setHierarchyLegend(List.of("one","two","three"));
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

        Variant var1 = new Variant();
        var1.setText("var1 q1");
        Variant var2 = new Variant();
        var2.setText("var2 q1");
        Variant var3 = new Variant();
        var3.setText("var1 q2");
        Variant var4 = new Variant();
        var4.setText("var2 q2");

        Question q1 = new Question();
        Question q2 = new Question();
        q1.setVariants(List.of(var1, var2));
        q2.setVariants(List.of(var3, var4));

        Answer a1 = new Answer();
        a1.setClosedAnswer(List.of(var1, var2));
        Answer a2 = new Answer();
        a2.setClosedAnswer(List.of(var4));

        q1.setRightAnswer(a1);
        q2.setRightAnswer(a2);

        Test test = new Test();
        test.setTimeLimit(10L);
        test.setQuestions(List.of(q1,q2));
        test.setOpenedFor(Set.of(user1,user2));
        test.setAutoCheck(true);
        test.setSubject(subj);
        test.setCreator(user4);

        testRepo.save(test);
    }

    public void loadTestDataAnsw() {
        User user1 = userService.getUser(1L);

        Optional<Test> testOpt = testRepo.findById(1L);

        Test test = testOpt.orElseGet(Test::new);


        List<Question> questions = test.getQuestions();

        Question q1 = questions.get(0);
        Question q2 = questions.get(1);
        List<Variant> var1 = q1.getVariants();
        List<Variant> var2 = q2.getVariants();

        ScoredAnswer sa1 = new ScoredAnswer();
        ScoredAnswer sa2 = new ScoredAnswer();

        Answer a1 = new Answer();
        a1.setClosedAnswer(List.of(var1.get(0), var1.get(1)));
        Answer a2 = new Answer();
        a2.setClosedAnswer(List.of(var2.get(1)));

        sa1.setScore(5);
        sa1.setQuestion(q1);
        sa1.setAnswer(a1);

        sa2.setScore(4);
        sa2.setQuestion(q2);
        sa2.setAnswer(a2);


        TestAnswer answ = new TestAnswer();

        answ.setTest(test);
        answ.setAnswers(List.of(sa1, sa2));
        answ.setValidated(true);
        answ.setStudent(user1);
        answ.setTotalScore(10);
        answ.setComment("abobius");

        testAnswerRepo.save(answ);

    }

    @ResponseBody
    @RequestMapping(value = "/{ID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Subject main(
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
            loadTest();
            loadTestDataAnsw();

            loadHierarchy();
            loaded = true;
        }


        Boolean isAscB = isAsc.orElse(Boolean.TRUE);
        Sort.Direction dir = isAscB?Sort.Direction.ASC : Sort.Direction.DESC;

        Subject subj = subjectService.getSubjectByName("math");
        User user = userService.findUserByEmail("user@mail.ru");
        subj = subjectService.addTeacherToSubject(subj, user.getId());
        System.out.println(user);
        //return subjectService.getSubjectsPage(ID, dir, page.orElse(0), sortBy.orElse("id"));
        return subj;
    }

}
