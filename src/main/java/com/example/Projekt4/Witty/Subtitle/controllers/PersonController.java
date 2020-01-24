package com.example.Projekt4.Witty.Subtitle.controllers;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import com.example.Projekt4.Witty.Subtitle.domain.PersonJSON;
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

@Controller("personwebcontroller")
@Slf4j
public class PersonController {

    private PersonManager pm;

    private CompanyManager cm;

    @Autowired
    public PersonController(PersonManager pm, CompanyManager cm) {
        this.cm = cm;
        this.pm = pm;
        cm.setPm(pm);
        pm.setCm(cm);
    }

    @GetMapping("/person")
    public String home(Model model) {
        pm.resolveDependencies();
        model.addAttribute("persons", pm.getAllPersons());
        return "person-all";
    }

    @GetMapping("/person/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "person-add";
    }

    @PostMapping("/person/add")
    public String personAdd(@Valid final Person person, final Errors errors, final Model model) {
        if (errors.hasErrors()) {
            return "person-add";
        }
        Company comp = cm.findByCompanyName(person.getComp().getCompanyName());
        if (comp != null) {
            person.setComp(comp);
        }
        System.out.println(person);
        pm.addPerson(person);
        model.addAttribute("person", new Person());
        return "person-add";
    }

    @GetMapping("/person/delete/{id}")
    public String deletePerson(@PathVariable("id") int id, Model model) {
        pm.remove(id);
        pm.resolveDependencies();
        model.addAttribute("persons", pm.getAllPersons());
        return "person-all";
    }

    @GetMapping("/person/edit/{id}")
    public String newEditedPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", pm.findById(id));
        return "person-edit";
    }

    @PostMapping("/person/edit")
    public String editPerson(@Valid Person person, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "person-edit";
        }
        pm.replacePerson(person.getId(), person);
        pm.resolveDependencies();
        model.addAttribute("persons", pm.getAllPersons());
        return "person-all";
    }

    @GetMapping("/person/json/{id}")
    public String newPersonJson(@PathVariable("id") int id, Model model) throws JsonProcessingException {
        Person p = pm.findById(id);
        PersonJSON pj = new PersonJSON(id, p.getFirstName(), p.getLastName(), p.getEmail(), p.getCompanyName(), p.getDoe());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(pj);
        model.addAttribute("personjson", jsonString);
        return "person-json";
    }

    @GetMapping("/person/csv")
    public String personCsv(Model model) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        String OBJECT_LIST_SAMPLE = "./persons-export.csv";
        List<PersonJSON> pj = new ArrayList<>();
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(OBJECT_LIST_SAMPLE));
        ) {

            StatefulBeanToCsv<PersonJSON> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            List<Person> p = pm.getAllPersons();

            for (Person person : p) {
                pj.add(new PersonJSON(person.getId(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getCompanyName(), person.getDoe()));
            }

            beanToCsv.write(pj);
            log.info("Eksportowano pliks csv.");

        }

        model.addAttribute("persons", pm.getAllPersons());
        return "person-all";
    }


}
