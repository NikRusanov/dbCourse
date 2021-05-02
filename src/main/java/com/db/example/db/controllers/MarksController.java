package com.db.example.db.controllers;

import com.db.example.db.entities.Mark;
import com.db.example.db.services.MarksService;
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

    private final MarksService marksService;

    public MarksController(MarksService marksService) {
        this.marksService = marksService;
    }


    @GetMapping("/marks")
    public String listMark(Model model) {
        List<Mark> marks = marksService.list();
        model.addAttribute("marks", marks);
        return "marks";
    }

    @GetMapping("/marks/new")
    public String showMarkAddForm(Model model) {
        model.addAttribute("mark", new Mark());
        return  "mark_form";
    }

    @PostMapping("/marks/save")
    public String saveMark(Mark mark) {
        marksService.save(mark);
        return "redirect:/marks";
    }

    @GetMapping("marks/edit/{id}")
    public String showMarkEditForm(@PathVariable("id") Integer id, Model model) {
        Mark mark = marksService.findById(id);
        if(mark == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\"Unable to find people with id" + id);
        }
        model.addAttribute("mark", mark);
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
