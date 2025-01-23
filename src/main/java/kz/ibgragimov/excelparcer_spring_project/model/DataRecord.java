package kz.ibgragimov.excelparcer_spring_project.model;

import java.time.LocalDate;

public class DataRecord {
    private Long transportCard;
    private Long iin;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;

    public DataRecord(){

    }

    public DataRecord(Long transportCard, Long iin, String firstName, String middleName, String lastName, LocalDate dateOfBirth) {
        this.transportCard = transportCard;
        this.iin = iin;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
