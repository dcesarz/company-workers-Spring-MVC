package com.example.Projekt4.Witty.Subtitle.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class Company {


    @CsvBindByName(column = "Company")
    @Pattern(regexp = "^[A-Z a-z]{2,50}$", message = "Company name is either too long, too short, or contains forbidden characters.")
    private String companyName;

    private int id;

    private Person ceo;

    private String uniq;

    public Company() {

    }

    public Company(String companyName) {
        this.companyName = companyName;
        this.id = -1;
    }


    @Override
    public String toString() {
        return "Company{" +
                ", companyName='" + companyName + '\'' +
                ", id=" + id +
                ", ceo=" + ceo +
                ", uniq='" + uniq + '\'' +
                '}';
    }

}
