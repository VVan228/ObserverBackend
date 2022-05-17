package ru.isu.observer;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.hierarchy.HierarchyLeaf;
import ru.isu.observer.model.test.*;
import ru.isu.observer.model.user.User;
import ru.isu.observer.service.HierarchyService;
import ru.isu.observer.service.TestValidationService;

import java.util.*;



@SpringBootTest
public class HierarchyServiceTests {
    Long id = 0L;

    @Autowired
    HierarchyService hierarchyService;

    @Test
    public void getStudentsWithAccessTest(){
        List<Hierarchy> leaves = getArrayOfStudents(27);

        List<Hierarchy> branchesLvl1 = getArrayOfUsers(9);
        for(int i = 0; i<9; i++){
            branchesLvl1.get(i).setChildren(new HashSet<>(leaves.subList(i*3, i*3+3)));
        }

        List<Hierarchy> branchesLvl2 = getArrayOfUsers(3);
        for(int i = 0; i<3; i++){
            branchesLvl2.get(i).setChildren(new HashSet<>(branchesLvl1.subList(i*3, i*3+3)));
        }

        Hierarchy root = new Hierarchy();
        root.setId(id++);
        root.setChildren(new HashSet<>(branchesLvl2));

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

    public List<Hierarchy> getArrayOfUsers(int size) {
        List<Hierarchy> res = new ArrayList<>();

        for(int i = 0; i<size; i++){
            Hierarchy h = new Hierarchy();
            h.setId(id++);
            res.add(h);
        }

        return res;
    }

    public List<Hierarchy> getArrayOfStudents(int size) {
        List<Hierarchy> res = new ArrayList<>();

        for(int i = 0; i<size; i++){
            HierarchyLeaf h = new HierarchyLeaf();
            User user = new User();
            user.setEmail(""+i);
            h.setId(id++);
            h.setStudent(user);
            res.add(h);
        }

        return res;
    }
}

