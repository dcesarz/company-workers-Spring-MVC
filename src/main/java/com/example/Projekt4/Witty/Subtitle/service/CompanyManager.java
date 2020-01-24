package com.example.Projekt4.Witty.Subtitle.service;

import com.example.Projekt4.Witty.Subtitle.domain.Company;

import java.util.List;

public interface CompanyManager {

    void addCompany(Company company);

    void resolveDependencies();

    Company findByUniq(String uniq);

    List<Company> getAllCompanies();

    void remove(String companyName);

    Company findByCompanyName(String companyName);

    int findIndex(String companyName);

    int findIndexUniq(String uniq);

    void replaceCompany(String companyName, Company company);

    void setPm(PersonManager pm);

}
