package com.example.Projekt4.Witty.Subtitle.service;


import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.thoughtworks.xstream.XStream;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PersonInMemoryService implements PersonManager {


    private CompanyManager cm;

    private static List<Person> persons = new ArrayList<>();


    public PersonInMemoryService() throws IOException {
        List<Person> persons;
        String fileName = "src/main/resources/MOCK_DATA.csv";
        String outXMLper = "src/main/resources/parsed_person_beans.xml";
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Person> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Person.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Person.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            persons = csvToBean.parse();

            XStream xstream2 = new XStream();

            xstream2.toXML(persons, new FileWriter(outXMLper, false));

            persons.forEach(System.out::println);
        }
        for (Person p : persons) {
            addPerson(p);
        }
        this.cm = new CompanyInMemoryService();
    }


    public void setCm(CompanyManager cm) {
        this.cm = cm;
    }

    public void addPerson(Person person) {
        if (persons.isEmpty()) {
            person.setId(0);
        } else {
            SortPersonsById();
            person.setId(persons.get(persons.size() - 1).getId() + 1);
        }
        try {
            Company c = cm.findByCompanyName(person.getComp().getCompanyName());
            if (c.getCeo().getId() == person.getId()) {
                c.setCeo(person);
                cm.replaceCompany(c.getCompanyName(), c);
            }
        } catch (NullPointerException e) {
            System.out.println("Nie udało się znaleźć company o podanej nazwie. Pole pozostawiono dla wygody edycji.");
        }
        persons.add(person);
    }


    public void SortPersonsById() {
        persons.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getId() > o2.getId()) {
                    return 1;
                }
                if (o1.getId() < o2.getId()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public Person findById(int id) {
        for (Person person : persons) {
            if (person.getId() == id) {
                return person;
            }

        }
        return null;
    }

    @Override
    public List<Person> findByCompany(String companyName) {
        List<Person> workers = new ArrayList<>();

        for (Person person : persons) {
            if (person.getCompanyName() != null) {
                if (person.getCompanyName().equals(companyName)) {
                    workers.add(person);
                }
            }

        }
        return workers;
    }

    @Override
    public int findIndex(int id) {
        int i;
        int size = persons.size();

        for (i = 0; i < size; i++) {
            if (persons.get(i).getId() == id) {
                return i;
            }

        }
        return -1;
    }

    @Override
    public void remove(int id) {
        int z = findIndex(id);
        if (z != -1) {
            try {
                List<Company> c = cm.getAllCompanies();
                for (Company company : c) {
                    if (company.getCeo() != null) {
                        if (company.getCeo().getId() == id) {
                            company.setCeo(null);
                        }
                    }
                }
            }
            catch(NullPointerException e)
            {
                System.out.println("Lista c jest pusta : ((((");
            }
            persons.remove(z);
        } else {
            System.out.println("Nie udało się usunąć person. Person nie istnieje.");
        }
    }

    @Override
    public List<Person> getAllPersons() {
        SortPersonsById();
        return persons;
    }

    @Override
    public void resolveDependencies() {
        List<Person> personList = getAllPersons();
        List<Company> companyList = cm.getAllCompanies();
        for (Company c : companyList) {
            for (Person p : personList) {
                try {
                    if (c.getCompanyName().equals(p.getCompanyName())) {
                        p.setComp(c);

                    }
                } catch (NullPointerException e) {
                }
            }
        }
    }

    @Override
    public void replacePerson(int id, Person person) {
        person.setId(id); //for tests. look up whats wrong in controller.
        int z = findIndex(id);
        if (z != -1) {
            persons.set(z, person);
            List<Company> c = cm.getAllCompanies();
            for (Company company : c) {
                if (company.getCeo() != null) {
                    if (company.getId() == id) {
                        company.setCeo(person);
                    }
                }
            }
        } else {
            System.out.println("Nie udało się edytować Person, ponieważ oryginał nie istnieje.");
        }
    }

}
