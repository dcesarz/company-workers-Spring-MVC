package com.example.Projekt4.Witty.Subtitle.controllers;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
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
public class CompanyWorkersSearchController {

    private PersonManager pm;


    @Autowired
    public CompanyWorkersSearchController(PersonManager pm, CompanyManager cm) {
        this.pm = pm;
    }


    @GetMapping("/searchworkers")
    public String findWorkers(Model model) {
        model.addAttribute("company", new Company());
        return "company-workers-search";
    }

    @PostMapping("/searchworkers/result")
    public String findWorkers(Company company, Model model) {
        pm.resolveDependencies();
        model.addAttribute("company", company);
        model.addAttribute("persons", pm.findByCompany(company.getCompanyName()));
        return "company-workers-result";
    }
}
