package com.example.Projekt4.Witty.Subtitle.controllers;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.GraphData;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import com.example.Projekt4.Witty.Subtitle.service.CompanyManager;
import com.example.Projekt4.Witty.Subtitle.service.PersonManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class GraphController {

    private PersonManager pm;

    private CompanyManager cm;

    @Autowired
    public GraphController(CompanyManager cm, PersonManager pm) {
        this.cm = cm;
        this.pm = pm;
        cm.setPm(pm);
        pm.setCm(cm);
    }

    @GetMapping("/graph")
    public String home(Model model) {
        cm.resolveDependencies();
        pm.resolveDependencies();
        model.addAttribute("companies", cm.getAllCompanies());
        model.addAttribute("persons", pm.getAllPersons());
        return "graph";
    }

    @RequestMapping("/getgraph")
    @ResponseBody
    public GraphData getGraph() {
        GraphData data = new GraphData();
        HashMap<String, Integer> workers = new HashMap<>();
        List<Company> c = cm.getAllCompanies();
        List<Person> lp;
        for (Company company : c) {
            lp = pm.findByCompany(company.getCompanyName());
            if (lp != null) {
                workers.put(company.getCompanyName(), lp.size());
            }
        }

        Map<Object, Object> map;
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();

        for (Map.Entry<String, Integer> entry : workers.entrySet()) {
            map = new HashMap<>();
            map.put("label", entry.getKey());
            map.put("y", entry.getValue());
            list.add(map);
        }

        data.setWorkers(list);
        return data;
    }


}
