package com.example.Projekt4.Witty.Subtitle.controllers;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.CompanyJSON;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import com.example.Projekt4.Witty.Subtitle.service.CompanyManager;
import com.example.Projekt4.Witty.Subtitle.service.PersonManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller("companywebcontroller")
@Slf4j
public class CompanyController {

    private PersonManager pm;

    private CompanyManager cm;

    @Autowired
    public CompanyController(CompanyManager cm, PersonManager pm) {
        this.cm = cm;
        this.pm = pm;
        cm.setPm(pm);
        pm.setCm(cm);
    }

    @GetMapping("/company")
    public String home(Model model) {
        cm.resolveDependencies();
        model.addAttribute("companies", cm.getAllCompanies());
        model.addAttribute("persons", pm.getAllPersons());
        System.out.println(cm.getAllCompanies());
        return "company-all";
    }

    @GetMapping("/company/new")
    public String newCompany(Model model) {
        model.addAttribute("company", new Company());
        return "company-add";
    }

    @PostMapping("/company/add")
    public String companyAdd(@Valid final Company company, final Errors errors, final Model model) {
        if (errors.hasErrors()) {
            return "company-add";
        }
        Person p = pm.findById(company.getCeo().getId());
        if (p != null) {
            company.setCeo(pm.findById(company.getCeo().getId()));
        }
        cm.addCompany(company);
        cm.resolveDependencies();
        model.addAttribute("companies", cm.getAllCompanies());
        return "company-all";
    }


    @GetMapping("/company/delete/{name}")
    public String deleteCompany(@PathVariable("name") String name, Model model) {
        cm.remove(name);
        cm.resolveDependencies();
        model.addAttribute("companies", cm.getAllCompanies());
        return "company-all";
    }

    @GetMapping("/company/edit/{uniq}")
    public String newEditedCompany(@PathVariable("uniq") String uniq, Model model) {
        model.addAttribute("company", cm.findByUniq(uniq));
        return "company-edit";
    }

    @PostMapping("/company/edit")
    public String editCompany(@Valid Company company, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "company-edit";
        }
        cm.replaceCompany(company.getUniq(), company);
        model.addAttribute("companies", cm.getAllCompanies());
        return "company-all";
    }

    @GetMapping("/company/json/{name}")
    public String newPersonJson(@PathVariable("name") String name, Model model) throws JsonProcessingException {
        Company c = cm.findByCompanyName(name);
        CompanyJSON cj = new CompanyJSON(name, c.getCeo(), pm.findByCompany(name).size());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(cj);
        model.addAttribute("companyjson", jsonString);
        return "company-json";
    }

    @GetMapping("/company/csv")
    public String companyCsv(Model model) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        String OBJECT_LIST_SAMPLE = "./companies-export.csv";
        List<CompanyJSON> cj = new ArrayList<>();
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(OBJECT_LIST_SAMPLE));
        ) {

            StatefulBeanToCsv<CompanyJSON> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            List<Company> c = cm.getAllCompanies();

            for (Company company : c) {
                cj.add(new CompanyJSON(company.getCompanyName(), company.getCeo(), pm.findByCompany(company.getCompanyName()).size()));
            }

            beanToCsv.write(cj);
            log.info("Eksportowano pliks csv.");

        }

        model.addAttribute("companies", cm.getAllCompanies());
        return "company-all";
    }

}
