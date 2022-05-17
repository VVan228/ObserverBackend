package ru.isu.observer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    UserRepo userRepo;
    HierarchyService hierarchyService;
    SubjectRepo subjectRepo;
    OrganisationRepo organisationRepo;
    TestRepo testRepo;
    AnswerRepo answerRepo;
    QuestionRepo questionRepo;
    TestAnswerRepo testAnswerRepo;
    ScoredAnswerRepo scoredAnswerRepo;

    @Autowired
    public HomeController(UserRepo userRepo,
                          HierarchyService hierarchyService,
                          SubjectRepo subjectRepo,
                          OrganisationRepo organisationRepo,
                          TestRepo testRepo,
                          AnswerRepo answerRepo,
                          QuestionRepo questionRepo,
                          TestAnswerRepo testAnswerRepo,
                          ScoredAnswerRepo scoredAnswerRepo) {
        this.userRepo = userRepo;
        this.hierarchyService = hierarchyService;
        this.subjectRepo = subjectRepo;
        this.organisationRepo = organisationRepo;
        this.testRepo = testRepo;
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.testAnswerRepo = testAnswerRepo;
        this.scoredAnswerRepo = scoredAnswerRepo;


    }

    public void loadUsers(){
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword("user");
        user.setRole(Role.STUDENT);
        user.setName("user");

        User user2 = new User();
        user2.setEmail("user2@mail.ru");
        user2.setPassword("user2");
        user2.setRole(Role.STUDENT);
        user2.setName("user2");

        User admin = new User();
        admin.setEmail("admin@mail.ru");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        admin.setName("admin");

        User teacher = new User();
        teacher.setEmail("teacher@mail.ru");
        teacher.setPassword("teacher");
        teacher.setRole(Role.TEACHER);
        teacher.setName("teacher");

        userRepo.save(user);
        userRepo.save(user2);
        userRepo.save(teacher);
        userRepo.save(admin);
    }

    public void loadHierarchy(){

        User student1 = userRepo.getByEmail("user@mail.ru");
        User student2 = userRepo.getByEmail("user2@mail.ru");
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
        User teacher = userRepo.getByEmail("teacher@mail.ru");

        Subject subject = new Subject();
        subject.setName("math");
        subject.addTeacher(teacher);

        subjectRepo.save(subject);
    }

    public void loadOrganisation(){

        User admin = userRepo.getByEmail("admin@mail.ru");

        Organisation org = new Organisation();
        org.setAdministrator(admin);
        org.setHierarchyLegend(List.of("one","two","three"));
        org.setName("isu");

        organisationRepo.save(org);

    }

    public void setOrganisationToUsers(){

        User student1 = userRepo.getByEmail("user@mail.ru");
        User student2 = userRepo.getByEmail("user2@mail.ru");
        User teacher = userRepo.getByEmail("teacher@mail.ru");
        User admin = userRepo.getByEmail("admin@mail.ru");
        Organisation org = organisationRepo.getByName("isu");

        student1.setOrganisation(org.getId());
        student2.setOrganisation(org.getId());
        admin.setOrganisation(org.getId());
        teacher.setOrganisation(org.getId());

        userRepo.save(student2);
        userRepo.save(student1);
        userRepo.save(teacher);
        userRepo.save(admin);
    }

    public void loadTest(){
        Optional<User> user1opt = userRepo.findById(1L);
        Optional<User> user2opt = userRepo.findById(2L);
        Optional<User> user3opt = userRepo.findById(3L);
        Optional<User> user4opt = userRepo.findById(4L);

        User user1 = user1opt.orElseGet(User::new);
        User user2 = user2opt.orElseGet(User::new);
        User user3 = user3opt.orElseGet(User::new);
        User user4 = user4opt.orElseGet(User::new);

        Optional<Subject> subjOpt = subjectRepo.findById(1L);

        Subject subj = subjOpt.orElseGet(Subject::new);

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
        Optional<User> user1opt = userRepo.findById(1L);

        User user1 = user1opt.orElseGet(User::new);

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
    public Hierarchy main(@PathVariable Long ID) {

        loadUsers();
        loadOrganisation();
        setOrganisationToUsers();

        loadSubject();
        loadTest();
        loadTestDataAnsw();

        loadHierarchy();

        return hierarchyService.getHierarchy(2L);
    }

}
