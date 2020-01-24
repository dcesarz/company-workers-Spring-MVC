package com.example.Projekt4.Witty.Subtitle.domain;

import lombok.Data;

@Data
public class CompanyJSON {
    private String companyName;
    private String ceo;
    private int workers;

    public CompanyJSON(String companyName, Person ceo, int workers) {
        this.companyName = companyName;
        if (ceo != null) {
            this.ceo = ceo.getFirstName() + " " + ceo.getLastName();
        } else {
            this.ceo = "No ceo";
        }
        this.workers = workers;
    }


}
