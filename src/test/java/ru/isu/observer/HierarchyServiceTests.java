package ru.isu.observer;


import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.hierarchy.HierarchyBranch;
import ru.isu.observer.model.hierarchy.HierarchyBranchPlain;
import ru.isu.observer.model.hierarchy.HierarchyLeaf;
import ru.isu.observer.model.test.*;
import ru.isu.observer.model.user.User;
import ru.isu.observer.service.HierarchyService;
import ru.isu.observer.service.TestValidationService;
import ru.isu.observer.service.UserService;

import java.util.*;



@SpringBootTest
public class HierarchyServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    HierarchyService hierarchyService;

    @Test
    public void getStudentsWithAccessTest(){
        List<HierarchyLeaf> leaves = getArrayOfStudents(27);

        List<HierarchyBranch> branchesLvl1 = getArrayOfUsers(9);
        for(int i = 0; i<9; i++){
            for(int j = i*3; j<i*3+3; j++){
                branchesLvl1.get(i).addChild(leaves.get(j));
                //System.out.println(branchesLvl1.get(i).getId());
                userService.saveStudent(leaves.get(j).getStudent());
            }
        }

        List<HierarchyBranch> branchesLvl2 = getArrayOfUsers(3);
        for(int i = 0; i<3; i++){
            for(int j = i*3; j<i*3+3; j++){
                branchesLvl2.get(i).addChild(branchesLvl1.get(j));
            }
        }

        Hierarchy root = new Hierarchy();
        for (HierarchyBranch hierarchyBranch : branchesLvl2) {
            root.addChild(hierarchyBranch);
        }

        //hierarchyService.createHierarchy(root);

        assert hierarchyService.getStudentsWithAccess(List.of(root)).size()==27;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl2.get(1))).size()==18;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(1), branchesLvl2.get(2))).size()==18;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl2.get(2))).size()==18;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl1.get(0))).size()==9;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl1.get(1))).size()==9;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl1.get(2))).size()==9;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl1.get(3))).size()==12;
        assert hierarchyService.getStudentsWithAccess(List.of(branchesLvl2.get(0), branchesLvl1.get(3), branchesLvl1.get(4))).size()==15;
        assert hierarchyService.getStudentsWithAccess(List.of(root, branchesLvl2.get(0), branchesLvl1.get(0))).size()==27;
    }


    @Test
    public void hierarchyGetLevelTest(){


        List<HierarchyBranchPlain> res = hierarchyService.getLevelOfHierarchy(1L, 1);
        assert res.size()==9;


    }


    public List<HierarchyBranch> getArrayOfUsers(int size) {
        List<HierarchyBranch> res = new ArrayList<>();
        Faker faker = new Faker();

        for(int i = 0; i<size; i++){
            HierarchyBranch h = new HierarchyBranch();
            h.setName(faker.company().name());
            res.add(h);
        }

        return res;
    }

    public List<HierarchyLeaf> getArrayOfStudents(int size) {
        List<HierarchyLeaf> res = new ArrayList<>();
        Faker faker = new Faker();

        for(int i = 0; i<size; i++){
            HierarchyLeaf h = new HierarchyLeaf();
            User user = new User();
            user.setEmail(faker.internet().safeEmailAddress());
            user.setName(faker.name().name());
            h.setStudent(user);
            res.add(h);
        }

        return res;
    }
}

