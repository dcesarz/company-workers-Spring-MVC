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
public class CompanySearchController {

    private CompanyManager cm;

    @Autowired
    public CompanySearchController(PersonManager pm, CompanyManager cm) {
        this.cm = cm;
    }


    @GetMapping("/searchcompany")
    public String findCompany(Model model) {
        model.addAttribute("company", new Company());
        return "company-search";
    }

    @PostMapping("/searchcompany/result")
    public String findCompany(Company company, Model model) {
        cm.resolveDependencies();
        model.addAttribute("company", cm.findByCompanyName(company.getCompanyName()));
        return "company-search-result";
    }

}
