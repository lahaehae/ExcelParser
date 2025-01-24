package kz.ibragimov.excelparser_spring_project.models;

import java.time.LocalDate;

public class DataRecord {
    private Long transportCard;
    private Long iin;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;

    public Long getTransportCard() {
        return transportCard;
    }

    public void setTransportCard(Long transportCard) {
        this.transportCard = transportCard;
    }

    public Long getIin() {
        return iin;
    }

    public void setIin(Long iin) {
        this.iin = iin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
