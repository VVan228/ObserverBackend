package ru.isu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.isu.model.hierarchy.Branch;
import ru.isu.model.hierarchy.Leaf;
import ru.isu.model.hierarchy.Node;
import ru.isu.model.hierarchy.Root;
import ru.isu.repository.NodeRepository;

@Controller
public class HomeController {
    @Autowired
    NodeRepository nodeRepository;

    @ResponseBody
    @RequestMapping("/set")
    public String createComponent() {
        Node root = new Root();
        Node branch = new Branch();
        Node branch2 = new Branch();
        Node leaf = new Leaf();
        Node leaf2 = new Leaf();

        root.addChild(branch);
        root.addChild(branch2);

        branch.addChild(leaf);
        branch2.addChild(leaf2);

        nodeRepository.save(root);

        try{
            return  new ObjectMapper().writeValueAsString(root);
        }catch (JsonProcessingException e){
            return "error!!! " + e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/get/{id}")
    public String getComponent(@PathVariable Long id){
        try{
            Node c = nodeRepository.findOne(id);
            return new ObjectMapper().writeValueAsString(c);
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
