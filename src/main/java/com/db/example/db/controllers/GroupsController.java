package com.db.example.db.controllers;

import com.db.example.db.entities.Group;
import com.db.example.db.entities.People;
import com.db.example.db.services.GroupsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class GroupsController {
    private final GroupsService groupsService;


    public  GroupsController(GroupsService groupsService) {
        this.groupsService = groupsService;
    }


    @GetMapping("/groups")
    public String listGroup(Model model) {
        List<Group> groups = groupsService.list();
        model.addAttribute("groupsList", groups);
        return "groups";
    }

    @GetMapping("/groups/new")
    public String showGroupAddForm(Model model) {
        model.addAttribute("group", new Group());
        return  "group_form";
    }

    @PostMapping("/groups/save")
    public String saveGroup(Group group) {
        groupsService.save(group);
        return "redirect:/groups";
    }

    @GetMapping("groups/edit/{id}")
    public String showGroupEditForm(@PathVariable("id") Integer id, Model model) {
        Group group = groupsService.findById(id);
        if(group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find people with id" + id);
        }
        model.addAttribute("group", group);
        return "group_form";
    }

    @GetMapping("groups/delete/{id}")
    public String deleteGroup(@PathVariable("id") Integer id, Model model) {
        Group group = groupsService.findById(id);
        if(group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find group with id" + id);
        }
        groupsService.delete(group);
        return "redirect:/groups";
    }
}
