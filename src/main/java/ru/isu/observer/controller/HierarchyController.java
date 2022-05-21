package ru.isu.observer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import ru.isu.observer.model.global.Organisation;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.model.hierarchy.HierarchyBranchPlain;
import ru.isu.observer.model.hierarchy.HierarchyRoot;
import ru.isu.observer.responses.EntityError;
import ru.isu.observer.responses.EntityValidator;
import ru.isu.observer.service.HierarchyService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HierarchyController {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;
    HierarchyService hierarchyService;
    SmartValidator validator;


    @Autowired
    public HierarchyController(HierarchyService hierarchyService, SmartValidator validator) {
        this.hierarchyService = hierarchyService;
        this.validator = validator;
    }
    Sort.Direction getDir(Optional<Boolean> isAsc){
        Boolean isAscB = isAsc.orElse(Boolean.TRUE);
        return isAscB?Sort.Direction.ASC : Sort.Direction.DESC;
    }


    @ResponseBody
    @RequestMapping(
            value="/hierarchy/save",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public ResponseEntity<List<EntityError>> saveSubject(
            @Valid @RequestBody Map<String,Object> bod,
            BindingResult result
    ){
        //get org from user
        HierarchyRoot h = hierarchyService.getHierarchyFromMap(bod, 1L);
        validator.validate(h, result);
        if(!result.hasErrors()){
            hierarchyService.createHierarchy(h);
        }
        return EntityValidator.validate(result);
    }

    @ResponseBody
    @RequestMapping(
            value="/hierarchy/update/addStudent/{hierarchyId}/{studentId}",
            method = RequestMethod.POST
    )
    public void addStudent(
            @PathVariable Long hierarchyId,
            @PathVariable Long studentId
    ){
        hierarchyService.addStudentToHierarchy(hierarchyId, studentId);
    }

    @ResponseBody
    @RequestMapping(
            value="/hierarchy/get/byLevel/{level}"
    )
    public List<HierarchyBranchPlain> getByLevel(
            @PathVariable int level
    ){
        //will get org from user
        return hierarchyService.getLevelOfHierarchy(
                hierarchyService.getHierarchyIdByOrganisation(1L),
                level
        );
    }

    @ResponseBody
    @RequestMapping(
            value="/hierarchy/get/labels"
    )
    public List<String> getLabels(){
        //will get org from user
        return hierarchyService.getLabelsOfHierarchyByOrganisation(1L);
    }

    @ResponseBody
    @RequestMapping(
            value = "/hierarchy/get/fullTree"
    )
    public Hierarchy getFullTree(){
        //will get org from user
        return hierarchyService.getHierarchy(hierarchyService.getHierarchyIdByOrganisation(1L));
    }

}
