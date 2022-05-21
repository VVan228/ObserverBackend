package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.global.ListToStringConverter;
import ru.isu.observer.model.global.Organisation;
import ru.isu.observer.model.hierarchy.*;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.HierarchyRepo;
import ru.isu.observer.repo.OrganisationRepo;
import ru.isu.observer.repo.UserRepo;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class HierarchyService {

    HierarchyRepo hierarchyRepo;
    UserService userService;
    OrganisationRepo organisationRepo;

    @Autowired
    HierarchyService(HierarchyRepo hierarchyRepo, UserService userService, OrganisationRepo organisationRepo){
        this.hierarchyRepo = hierarchyRepo;
        this.userService = userService;
        this.organisationRepo = organisationRepo;
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

    public Long getHierarchyIdByOrganisation(Long organisationId){
        return hierarchyRepo.getHierarchyIdByOrganisation(organisationId);
    }

    public void addStudentToHierarchy(Hierarchy node, User user){
        Set<Long> usersInNode = getStudentsWithAccess(new ArrayList<>(List.of(node)));
        if(usersInNode.contains(user.getId())){
            throw new EntityNotFoundException("student is already added");
        }
        HierarchyLeaf newNode = new HierarchyLeaf();
        newNode.setStudent(user);
        node.addChild(newNode);
        hierarchyRepo.save(node);
    }
    public void addStudentToHierarchy(Long nodeId, Long userId){
        addStudentToHierarchy(getHierarchy(nodeId), userService.getUser(userId));
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


    @Transactional
    public HierarchyRoot getHierarchyFromMap(Map<String,Object> map, Long organisationId){
        //HierarchyRoot root =
        //System.out.println(map.get("children"));
        Organisation org;
        if(organisationRepo.existsById(organisationId)){
             org = organisationRepo.getById(organisationId);
        }else{
            throw new EntityNotFoundException("organisation not found");
        }

        HierarchyRoot root = new HierarchyRoot();
        root.setOrganisation(org);
        ((List<Map<String, Object>>) map.get("children")).forEach((ch)->root.addChild(parseMapHierarchy(ch)));
        org.setHierarchyLegend(((List<String>) map.get("legend")));
        organisationRepo.flush();

        return root;
    }
    private Hierarchy parseMapHierarchy(Map<String, Object> map){
        List<Map<String, Object>> childrenMap = (List<Map<String, Object>>) map.get("children");
        HierarchyBranch hierarchyBranch = new HierarchyBranch();
        hierarchyBranch.setName(((String) map.get("name")));

        if(childrenMap == null){
            return hierarchyBranch;
        }
        childrenMap.forEach((childMap)->hierarchyBranch.addChild(parseMapHierarchy(childMap)));

        return hierarchyBranch;
    }


}
