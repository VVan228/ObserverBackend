package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.global.ListToStringConverter;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.hierarchy.HierarchyBranchPlain;
import ru.isu.observer.model.hierarchy.HierarchyLeaf;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.HierarchyRepo;
import ru.isu.observer.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Transactional
    public void createHierarchy(Hierarchy hierarchy){
        System.out.println(hierarchy.getChildren().size());
        hierarchyRepo.save(hierarchy);
        setIds(hierarchy);
        hierarchyRepo.flush();
    }
    private void setIds(Hierarchy node){
        node.getChildren().forEach((child)->child.setParentId(node.getId()));
        node.getChildren().forEach(this::setIds);
    }

    public Hierarchy getHierarchy(Long id){
        return hierarchyRepo.getById(id);
    }

    public List<String> getLabelsOfHierarchyByOrganisation(Long organisationId){
        return new ListToStringConverter().convertToEntityAttribute(
                hierarchyRepo.getHierarchyLabelsByOrganisation(organisationId)
        );
    }

    public Hierarchy getHierarchyByOrganisation(Long organisationId){
        return getHierarchy(hierarchyRepo.getHierarchyIdByOrganisation(organisationId));
    }

    public void addStudentToHierarchy(Hierarchy node, User user){
        HierarchyLeaf newNode = new HierarchyLeaf();
        newNode.setStudent(user);
        node.addChild(newNode);
        hierarchyRepo.save(node);
    }



    public Set<Long> getStudentsWithAccess(List<Hierarchy> nodes){
        Set<Long> res = new HashSet<>();
        nodes.forEach((child)->getStudentByNode(child, res));
        return res;
    }
    private void getStudentByNode(Hierarchy node, Set<Long> result){
        if(node.getChildren().isEmpty()){
            result.add(((HierarchyLeaf)node).getStudent().getId());
            return;
        }
        node.getChildren().forEach((child)->getStudentByNode(child, result));
    }

    public List<HierarchyBranchPlain> getLevelOfHierarchy(Long rootId, int level){

        List<HierarchyBranchPlain> res = new ArrayList<>();
        List<Long> nodes = new ArrayList<>();

        switch (level){
            case (0) -> nodes = hierarchyRepo.getHierarchyLevel1(rootId);
            case (1) -> nodes = hierarchyRepo.getHierarchyLevel2(rootId);
            case (2) -> nodes = hierarchyRepo.getHierarchyLevel3(rootId);
            case (3) -> nodes = hierarchyRepo.getHierarchyLevel4(rootId);
        }
        for(Long id: nodes){
            res.add(new HierarchyBranchPlain(id, hierarchyRepo.getNameById(id)));
        }

        return res;

    }


}
