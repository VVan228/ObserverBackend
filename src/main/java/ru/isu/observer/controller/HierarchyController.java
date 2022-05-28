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
import ru.isu.observer.security.SecurityUser;
import ru.isu.observer.service.HierarchyService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
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
        SecurityUser ud = SecurityUser.getCurrent();

        if(hierarchyService.getHierarchyIdByOrganisation(ud.getUser().getOrganisationId())!=null){
            throw new EntityNotFoundException("hierarchy existed");
        }

        HierarchyRoot h = hierarchyService.getHierarchyFromMap(bod, ud.getUser().getOrganisationId());
        validator.validate(h, result);
        if(!result.hasErrors()){
            hierarchyService.createHierarchy(h);
        }
        return EntityValidator.validate(result);
    }

    @ResponseBody
    @RequestMapping(
            value="/hierarchy/update/setLabels",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void saveSubject(@RequestBody List<String> labels){
        SecurityUser ud = SecurityUser.getCurrent();
        hierarchyService.setLabels(labels, ud.getUser().getOrganisationId());
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
        SecurityUser ud = SecurityUser.getCurrent();
        if(hierarchyService.getHierarchyIdByOrganisation(ud.getUser().getOrganisationId())==null){
            throw new EntityNotFoundException("no hierarchy");
        }
        return hierarchyService.getLevelOfHierarchy(
                hierarchyService.getHierarchyIdByOrganisation(ud.getUser().getOrganisationId()),
                level
        );
    }

    @ResponseBody
    @RequestMapping(
            value="/hierarchy/get/labels"
    )
    public List<String> getLabels(){
        SecurityUser ud = SecurityUser.getCurrent();
        if(hierarchyService.getHierarchyIdByOrganisation(ud.getUser().getOrganisationId())==null){
            throw new EntityNotFoundException("no hierarchy");
        }
        return hierarchyService.getLabelsOfHierarchyByOrganisation(ud.getUser().getOrganisationId());
    }

    @ResponseBody
    @RequestMapping(
            value = "/hierarchy/get/fullTree"
    )
    public Hierarchy getFullTree(){
        SecurityUser ud = SecurityUser.getCurrent();
        if(hierarchyService.getHierarchyIdByOrganisation(ud.getUser().getOrganisationId())==null){
            throw new EntityNotFoundException("no hierarchy");
        }
        return hierarchyService.getHierarchy(hierarchyService.getHierarchyIdByOrganisation(ud.getUser().getOrganisationId()));
    }

}
