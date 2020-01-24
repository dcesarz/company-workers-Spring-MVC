package com.example.Projekt4.Witty.Subtitle.service;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Service
class PersonInMemoryServiceTest {

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
            fail("IO Exception stopped creation of pm");
        }
    }

    @Test
    void addPerson() {
            pm.addPerson(new Person("Person", "LastName", "Email", "10/10/2010"));
            List<Person> pl = pm.getAllPersons();
            Person p = pl.get(pl.size()-1);
            assertTrue(p!=null && p.getFirstName().equals("Person"));
    }

    @Test
    void sortPersonsById() {
        pm.SortPersonsById();
        List<Person> pl = pm.getAllPersons();
        int i = 0;
        for(Person p : pl)
        {
            if(p.getId()<i)
            {
                fail("Nie posortowano prawidłowo, lub występują duplikaty ID.");
            }
            i = p.getId();
        }
    }

    @Test
    void findById() {
        pm.addPerson(new Person("Person", "LastName", "Email", "10/10/2010"));
        List<Person> pl = pm.getAllPersons();
        Person p = pl.get(pl.size()-1);
        int id = p.getId();
        assertTrue(pm.findById(id)!=null);
    }

    @Test
    void findByCompany() {
        String uniq = UUID.randomUUID().toString();
        Person p = new Person("Person", "LastName", "Email", "10/10/2010");
        Company c = new Company(uniq);
        p.setCompanyName(c.getCompanyName());
        p.setComp(c);
        pm.addPerson(p);
        assertTrue(p.equals(pm.findByCompany(uniq).get(0)));
    }

    @Test
    void findIndex() {
        pm.addPerson(new Person("Person", "LastName", "Email", "10/10/2010"));
        List<Person> pl = pm.getAllPersons();
        Person p = pl.get(pl.size()-1);
        int id = p.getId();
        assertTrue(pm.findIndex(id) == pl.size()-1);
    }

    @Test
    void remove() {
        pm.addPerson(new Person("Person", "LastName", "Email", "10/10/2010"));
//        String uniq = UUID.randomUUID().toString();
//        Company c = new Company(uniq);
//        cm.addCompany(c);
        List<Person> pl = pm.getAllPersons();
        Person p = pl.get(pl.size()-1);
        int id = p.getId();
        assertTrue(pm.findById(id) != null);
        pm.remove(id);
        assertTrue(pm.findById(id) == null);
    }

    @Test
    void getAllPersons() {
        assertTrue(!pm.getAllPersons().isEmpty());
    }

    @Test
    void resolveDependencies() {
            String uniq = UUID.randomUUID().toString();
            Person p = new Person("Person", "LastName", "Email", "10/10/2010");
            Company c = new Company(uniq);
            p.setCompanyName(uniq);
            pm.addPerson(p);
            cm.addCompany(c);
            pm.setCm(cm);
            cm.setPm(pm);
            pm.resolveDependencies();
            assertTrue(p.getComp() == c);
    }

    @Test
    void replacePerson() {
        pm.setCm(cm);
        cm.setPm(pm);
        pm.addPerson(new Person("Person", "LastName", "Email", "10/10/2010"));
        List<Person> pl = pm.getAllPersons();
        Person p = pl.get(pl.size()-1);
        int id = p.getId();
        Person newp = new Person("UPDATED", "LastName", "Email", "10/10/2010");
        pm.replacePerson(id, newp);
        System.out.println(pm.findById(id));
        assertTrue(pm.findById(id).getFirstName().equals("UPDATED"));
    }
}