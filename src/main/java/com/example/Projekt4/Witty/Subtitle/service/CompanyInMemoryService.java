package com.example.Projekt4.Witty.Subtitle.service;

import com.example.Projekt4.Witty.Subtitle.domain.Company;
import com.example.Projekt4.Witty.Subtitle.domain.Person;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CompanyInMemoryService implements CompanyManager {


    private PersonManager pm;

    private static List<Company> companies = new ArrayList<>();

    @Autowired
    public CompanyInMemoryService() throws IOException {
        List<Company> companies;
        String fileName = "src/main/resources/MOCK_DATA.csv";
        String outXMLcomp = "src/main/resources/parsed_company_beans.xml";
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Company> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Company.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Company.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            companies = csvToBean.parse();

            companies.forEach(System.out::println);

            XStream xstream1 = new XStream();

            xstream1.toXML(companies, new FileWriter(outXMLcomp, false));
        }
        for (Company c : companies) {
            c.setId(c.getId() - 1);
            addCompany(c);
            System.out.println(c);
        }
    }

    public void setPm(PersonManager pm) {
        this.pm = pm;
    }


    public void addCompany(Company company) {
        if (findByCompanyName(company.getCompanyName()) != null) {
            System.out.println("Nie udało się dodać firmy. Istnieje juz o tej samej nazwie.");
        } else {
            try {
                pm.findById(company.getCeo().getId()).setComp(company);
            } catch (NullPointerException e) {
                System.out.println("Nie znaleziono pracownika o podanym id. Pozostawiono pole puste.");
            }
            company.setUniq(UUID.randomUUID().toString());
            companies.add(company);
        }
    }

    @Override
    public Company findByCompanyName(String companyName) {
        for (Company company : companies) {
            if (company.getCompanyName().equals(companyName)) {
                return company;
            }
        }
        return null;
    }

    @Override
    public int findIndex(String companyName) {
        int i;
        int size = companies.size();

        for (i = 0; i < size; i++) {
            if (companies.get(i).getCompanyName().equals(companyName)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int findIndexUniq(String uniq) {
        int i;
        int size = companies.size();

        for (i = 0; i < size; i++) {
            if (companies.get(i).getUniq().equals(uniq)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public Company findByUniq(String uniq) {
        for (Company company : companies) {
            if (company.getUniq().equals(uniq)) {
                return company;
            }
        }
        return null;
    }

    @Override
    public void remove(String companyName) {
        int z = findIndex(companyName);
        System.out.println(companyName);
        if (z != -1) {
            companies.remove(z);
            try {
                List<Person> p = pm.findByCompany(companyName);
                for (Person person : p) {
                    person.setComp(null);
                }
            }
            catch(NullPointerException e)
            {
                System.out.println("Error przy szukaniu pracowników danej firmy.");
            }

        } else {
            System.out.println("Nie udało się usunąć company. Company nie istnieje.");
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        return this.companies;
    }

    @Override
    public void resolveDependencies() {
        List<Person> personList = pm.getAllPersons();
        List<Company> companyList = getAllCompanies();
        for (Person p : personList) {
            for (Company c : companyList) {
                if (p.getId() == c.getId() && p.getCompanyName().equals(c.getCompanyName())) {
                    c.setCeo(p);
                }
            }
        }
    }


    @Override
    public void replaceCompany(String uniq, Company company) {
        int z = findIndexUniq(uniq);
        if (z != -1) {
            List<Person> p = pm.findByCompany(companies.get(z).getCompanyName());
            for (Person person : p) {
                person.setComp(company);
                person.setCompanyName(company.getCompanyName());
                if (person.getId() == company.getId()) {
                    company.setCeo(person);
                }
            }

            companies.set(z, company);
        } else {
            System.out.println("Nie udało się edytować Company, ponieważ oryginał nie istnieje.");
        }
    }

}
