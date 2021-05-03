package com.db.example.db.controllers;

import com.db.example.db.entities.Mark;
import com.db.example.db.entities.People;
import com.db.example.db.entities.Subject;
import com.db.example.db.services.MarksService;
import com.db.example.db.services.PeopleService;
import com.db.example.db.services.SubjectService;
import org.hibernate.boot.jaxb.hbm.spi.SubEntityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class MarksController {
    private final PeopleService peopleService;
    private final MarksService marksService;
    private final SubjectService subjectService;
    @Autowired
    public MarksController(PeopleService peopleService, MarksService marksService, SubjectService subjectService) {
        this.peopleService = peopleService;
        this.marksService = marksService;
        this.subjectService = subjectService;
    }

    @GetMapping("/marks")
    public String listMarks(Model model) {
        List<Mark> marks = marksService.list();
        model.addAttribute("markList", marks);
        return "marks";
    }

    @GetMapping("/marks/new")
    public String showMarksAddForm(Model model) {
        List<People> students = peopleService.findByType('s');
        List<People> teachers = peopleService.findByType('t');
        List<Subject> subjects = subjectService.list();

        model.addAttribute("mark", new Mark());
        model.addAttribute("teacherList", teachers);
        model.addAttribute("studentList", students);
        model.addAttribute("subjectList", subjects);
        return  "mark_form";
    }

    @PostMapping("/marks/save")
    public String saveMark(Mark mark) {
        marksService.save(mark);
        return "redirect:/marks";
    }

    @GetMapping("marks/edit/{id}")
    public String showMarkEditForm(@PathVariable("id") Integer id, Model model) {
        List<People> students = peopleService.findByType('s');
        List<People> teachers = peopleService.findByType('t');
        List<Subject> subjects = subjectService.list();
        Mark mark = marksService.findById(id);
        if(mark == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find mark with id" + id);
        }
        model.addAttribute("mark", mark);
        model.addAttribute("teacherList", teachers);
        model.addAttribute("studentList", students);
        model.addAttribute("subjectList", subjects);
        return "mark_form";
    }

    @GetMapping("marks/delete/{id}")
    public String deleteMark(@PathVariable("id") Integer id, Model model) {
        Mark mark = marksService.findById(id);
        if(mark == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find mark with id" + id);
        }
        marksService.delete(mark);
        return "redirect:/marks";
    }
}
