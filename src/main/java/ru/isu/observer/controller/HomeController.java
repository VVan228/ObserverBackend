package ru.isu.observer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.isu.observer.model.User;
import ru.isu.observer.model.hierarchy.Hierarchy;
import ru.isu.observer.repo.HierarchyRepo;
import ru.isu.observer.repo.UserRepo;

@Controller
public class HomeController {

    UserRepo userRepo;
    HierarchyRepo hierarchyRepo;

    @Autowired
    public HomeController(UserRepo userRepo, HierarchyRepo hierarchyRepo) {
        this.userRepo = userRepo;
        this.hierarchyRepo = hierarchyRepo;
    }

    @ResponseBody
    @RequestMapping("/")
    public String main() throws JsonProcessingException {
        Hierarchy root = new Hierarchy();
        Hierarchy branch = new Hierarchy();
        Hierarchy branch2 = new Hierarchy();
        Hierarchy leaf = new Hierarchy();
        Hierarchy leaf2 = new Hierarchy();

        root.addChild(branch);
        root.addChild(branch2);

        branch.addChild(leaf);
        branch2.addChild(leaf2);

        hierarchyRepo.save(root);

        return new ObjectMapper().writeValueAsString(root);
    }

    @ResponseBody
    @RequestMapping("/get/{ID}")
    public String get(@PathVariable Long ID) throws JsonProcessingException {

        Hierarchy root = hierarchyRepo.findById(ID).get();

        return new ObjectMapper().writeValueAsString(root);
    }
}
