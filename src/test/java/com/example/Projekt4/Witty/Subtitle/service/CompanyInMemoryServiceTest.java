package com.example.Projekt4.Witty.Subtitle.service;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Service
class CompanyInMemoryServiceTest {

    private PersonManager pm;
    private CompanyManager cm;


    @BeforeEach
    @Test
    @Autowired
    void setUp() {
        try {
            pm = new PersonInMemoryService();
            cm = new CompanyInMemoryService();
        } catch (IOException e) {
            fail("IO Exception stopped creation of pm or cm");
        }
    }

    @Test
    void addCompany() {
    cm.addCompany(new Company("company"));
    Company c = cm.findByCompanyName("company");
    assertTrue(c!=null && c.getCompanyName().equals("company"));
    }


    @Test
    void findByCompanyName() {
     cm.addCompany(new Company("company"));
     cm.addCompany(new Company("company2"));
     Company c = cm.findByCompanyName("company");
     Company c2 = cm.findByCompanyName("company2");
     assertTrue(c!=null && c.getCompanyName().equals("company"));
     assertTrue(c2!=null && c2.getCompanyName().equals("company2"));
    }
//
//    @Test
//    void findIndex() {
//        cm.addCompany(new Company("company"));
//        List<Company> cl = cm.getAllCompanies();
//        int index = cl.size()-1;
//        System.out.println(index);
//        assertTrue(cm.findIndex("company") == index);
//
//    }

    @Test
    void findIndexUniq() {
        Company c = new Company("company");
        cm.addCompany(c);
        List<Company> cl = cm.getAllCompanies();
        Company c1 = cm.findByCompanyName("company");
        String uniq = c1.getUniq();
        int index = cl.size()-1;
        assertTrue(cm.findIndexUniq(uniq) == index);
    }

//    @Test
//    void findByUniq() {
//        Company c =new Company("company");
//        cm.addCompany(c);
//        cm.addCompany(c);
//        Company c1 = cm.findByCompanyName("company");
//        String uniq = c.getUniq();
//        Company c2 = cm.findByUniq(uniq);
//        assertTrue(c2!=null && c2.getCompanyName().equals("company") && c2.getUniq().equals(uniq));
//    }

    @Test
    void remove() {
        cm.setPm(pm);
        pm.setCm(cm);
        Company c = new Company("company");
        Person n =new Person("Person", "LastName", "Email", "10/10/2010");
        n.setComp(c);
        n.setCompanyName("company");
        cm.addCompany(c);
        pm.addPerson(n);
        assertTrue(cm.findByCompanyName("company")!=null);
        assertTrue(pm.findByCompany("company")!=null);
        cm.remove("company");
        assertTrue(cm.findByCompanyName("company")==null);
        assertTrue(pm.findByCompany("company")!=null);
    }

    @Test
    void getAllCompanies() {
    assertTrue(!cm.getAllCompanies().isEmpty());
    }


    @Test
    void resolveDependencies() {
        Company c = new Company("company");
        Person p = new Person("Person", "LastName", "Email", "10/10/2010");
        p.setCompanyName("company");
        List<Person> pl = pm.getAllPersons();
        int id = pl.size();
        c.setId(id);
        pm.setCm(cm);
        cm.setPm(pm);
        cm.addCompany(c);
        pm.addPerson(p);
        cm.resolveDependencies();
        System.out.println(pm.findById(id));
        assertTrue(cm.findByCompanyName("company").getCeo()==pm.findById(id));
    }

    @Test
    void replaceCompany() {
        cm.setPm(pm);
        pm.setCm(cm);
        Company c = new Company("company");
        Person p = new Person("Person", "LastName", "Email", "10/10/2010");
        p.setCompanyName("company");
        Company c1 = new Company("UPDATED");
        cm.addCompany(c);
        pm.addPerson(p);
        String uniq = cm.findByCompanyName("company").getUniq();
        c1.setUniq(uniq);
        cm.replaceCompany(uniq, c1);
        assertTrue(!pm.findByCompany("UPDATED").isEmpty() && cm.findByUniq(uniq).getCompanyName().equals("UPDATED"));
    }
}