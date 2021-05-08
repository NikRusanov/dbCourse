package com.db.example.db.controllers;


import com.db.example.db.entities.Group;
import com.db.example.db.entities.People;
import com.db.example.db.services.GroupsService;
import com.db.example.db.services.PeopleService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Controller
public class PeopleController {

    private final PeopleService peopleService;
    private final GroupsService groupService;

    @Autowired
    public PeopleController(PeopleService service, GroupsService groupService) {
        this.peopleService = service;
        this.groupService = groupService;
    }

    @GetMapping("/peoples")
    public String listPeoples(Model model, String keyword) {
        List<People> peoples;
        if (keyword == null || keyword.isEmpty()) {
            peoples = peopleService.list();
        } else  {
            peoples = peopleService.findByName(keyword);
        }
        model.addAttribute("peoplesList", peoples);
        model.addAttribute("groupsList", groupService.list());
        return "peoples";
    }

    @GetMapping(value = "/peoples/group_filter")
    public String groupFilter(Model model, Integer group_id, String action) {
        if(action.equals("reset")) {
            return "redirect:/peoples";
        }
        Group group = groupService.findById(group_id);
        model.addAttribute("peoplesList", peopleService.findByGroup(group));
        model.addAttribute("groupsList", groupService.list());
        return "peoples";
    }


    @GetMapping("/peoples/new")
    public String showPeopleAddForm(Model model) {
        model.addAttribute("people", new People());
        model.addAttribute("groups", groupService.list());
        return  "people_form";
    }

    @PostMapping("/peoples/save")
    public String savePeople(People people) {
        peopleService.save(people);
        return "redirect:/peoples";
    }

    @GetMapping("/peoples/edit/{id}")
    public String showPeopleEditForm(@PathVariable("id") Integer id, Model model) {
        People people = peopleService.findById(id);
        if(people == null) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find people with id" + id);
        }
        model.addAttribute("people", people);
        model.addAttribute("groups", groupService.list());
        return "people_form";
    }

    @GetMapping("/peoples/delete/{id}")
    public String deletePeople(@PathVariable("id") Integer id, Model model) {
        People people = peopleService.findById(id);
        if(people == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find people with id" + id);
        }
        peopleService.delete(people);
        return "redirect:/peoples";
    }







}
