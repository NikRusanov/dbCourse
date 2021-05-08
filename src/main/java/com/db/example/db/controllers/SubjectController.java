package com.db.example.db.controllers;

import com.db.example.db.entities.Subject;
import com.db.example.db.services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public String listSubject(Model model) {
        List<Subject> subjects = subjectService.list();
        model.addAttribute("subjects", subjects);
        return "subjects";
    }

    @GetMapping("/subjects/new")
    public String showSubjectAddForm(Model model) {
        model.addAttribute("subject", new Subject());
        return  "subject_form";
    }

    @PostMapping("/subjects/save")
    public String saveSubject(Subject subject) {
        subjectService.save(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/subjects/edit/{id}")
    public String showPeopleEditForm(@PathVariable("id") Integer id, Model model) {
        Subject subject = subjectService.findById(id);
        if(subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find people with id" + id);
        }
        model.addAttribute("subject", subject);
        return "subject_form";
    }

    @GetMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable("id") Integer id, Model model) {
        Subject subject = subjectService.findById(id);
        if(subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find subject with id" + id);
        }
        subjectService.delete(subject);
        return "redirect:/subjects";
    }
}
