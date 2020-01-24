package com.example.Projekt4.Witty.Subtitle.service;

import com.example.Projekt4.Witty.Subtitle.domain.Person;

import java.util.List;

public interface PersonManager {

    void addPerson(Person person);

    void resolveDependencies();

    Person findById(int id);

    int findIndex(int id);

    List<Person> getAllPersons();

    void remove(int id);

    List<Person> findByCompany(String companyName);

    void replacePerson(int id, Person person);

    void SortPersonsById();

    void setCm(CompanyManager cm);
}
