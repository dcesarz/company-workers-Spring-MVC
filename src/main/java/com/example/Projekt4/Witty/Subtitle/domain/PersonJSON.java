package com.example.Projekt4.Witty.Subtitle.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class PersonJSON {
    @CsvBindByName(column = "id")
    private int id;
    @CsvBindByName(column = "first_name")
    private String firstName;
    @CsvBindByName(column = "last_name")
    private String lastName;
    @CsvBindByName(column = "email")
    private String email;
    @CsvBindByName(column = "company")
    private String companyName;
    @CsvBindByName(column = "date of employment")
    private String doe;

    public PersonJSON(int id, String firstName, String lastName, String email, String companyName, String doe) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.doe = doe;
    }
}
