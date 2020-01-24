package com.example.Projekt4.Witty.Subtitle.controllers;

import com.example.Projekt4.Witty.Subtitle.domain.Person;
import com.example.Projekt4.Witty.Subtitle.service.CompanyManager;
import com.example.Projekt4.Witty.Subtitle.service.PersonManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class PersonSearchController {

    private PersonManager pm;


    private CompanyManager cm;

    @Autowired
    public PersonSearchController(PersonManager pm, CompanyManager cm) {
        this.pm = pm;
        this.cm = cm;
    }


    @GetMapping("/searchperson")
    public String findPerson(Model model) {
        model.addAttribute("person", new Person());
        return "person-search";
    }

    @PostMapping("/searchperson/result")
    public String findPerson(Person person, Model model) {
        pm.resolveDependencies();
        model.addAttribute("person", pm.findById(person.getId()));
        return "person-search-result";
    }

}
