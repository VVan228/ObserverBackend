package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.hierarchy.HierarchyLeaf;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.HierarchyRepo;
import ru.isu.observer.repo.UserRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HierarchyService {

    HierarchyRepo hierarchyRepo;
    UserRepo userRepo;

    @Autowired
    HierarchyService(HierarchyRepo hierarchyRepo, UserRepo userRepo){
        this.hierarchyRepo = hierarchyRepo;
        this.userRepo = userRepo;
    }

    public void createHierarchy(Hierarchy hierarchy){
        hierarchyRepo.save(hierarchy);
        setIds(hierarchy);
        hierarchyRepo.save(hierarchy);
    }
    private void setIds(Hierarchy node){
        node.getChildren().forEach((child)->child.setParentId(node.getId()));
        node.getChildren().forEach(this::setIds);
    }

    public Hierarchy getHierarchy(Long id){
        return hierarchyRepo.getById(id);
    }


    public Set<Long> getStudentsWithAccess(List<Hierarchy> nodes){
        Set<Long> res = new HashSet<>();
        nodes.forEach((child)->getStudentByNode(child, res));
        return res;
    }
    private void getStudentByNode(Hierarchy node, Set<Long> result){
        if(node.getChildren().isEmpty()){
            result.add(((HierarchyLeaf)node).getStudent().getId());
            //result.add(userRepo.getById(node.getId()));
            return;
        }
        node.getChildren().forEach((child)->getStudentByNode(child, result));
    }

    /*public Set<User> getStudentsWithAccess(List<Hierarchy> nodes){
        Set<User> res = new HashSet<>();
        nodes.forEach((child)->getStudentByNode(child, res));
        return res;
    }
    private void getStudentByNode(Hierarchy node, Set<User> result){
        if(node.getChildren().isEmpty()){
            result.add(((HierarchyLeaf)node).getStudent());
            //result.add(userRepo.getById(node.getId()));
            return;
        }
        node.getChildren().forEach((child)->getStudentByNode(child, result));
    }*/


}
