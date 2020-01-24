package com.example.Projekt4.Witty.Subtitle.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class Person {

    @CsvBindByName(column = "id")
    @Min(value = 0)
    private int id;

    @CsvBindByName(column = "first_name")
    @Pattern(regexp = "^[A-Z a-z]{2,50}$", message = "Name is either too long or too short.")
    private String firstName;

    @CsvBindByName(column = "last_name")
    @Pattern(regexp = "^[A-Z a-z]{2,50}$", message = "Last name is either too long or too short.")
    private String lastName;

    @CsvBindByName(column = "email")
    @Pattern(regexp = "^(.+)@(.+)$", message = "Email is invalid")
    private String email;

    @CsvBindByName(column = "Company")
    private String companyName;

    private Company comp = null;

    @CsvBindByName(column = "Date of employment")
    @Pattern(regexp = "^([1-9]|1[012])/([1-9]|[12][0-9]|3[01])/(19|20)\\d\\d$", message = "Date is invalid.")
    private String doe;

    public Person() {
    }

    public Person(int id) {
        this.id = id;
    }

    public Person(String firstName, String lastName, String email, String doe) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.doe = doe;
    }

    @Override
    public String toString() {
        return "Person{" +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", companyName='" + companyName + '\'' +
                ", doe='" + doe + '\'' +
                '}';
    }

}
