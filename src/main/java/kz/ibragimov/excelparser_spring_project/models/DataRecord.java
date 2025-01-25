package kz.ibragimov.excelparser_spring_project.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public class DataRecord {
    @Min(value = 0, message = "Некорректный ввод номера Транспортной Карты")
    private Long transportCard;
    @Min(value = 12, message = "Некорректный ввод ИИН")
    @NotBlank(message = "Поле ИИН не может быть пустым")
    private Long iin;
    @NotBlank(message = "Поле Имя не может быть пустым")
    private String firstName;
    @NotBlank(message = "Поле Отчество не может быть пустым")
    private String middleName;
    @NotBlank(message = "Поле Фамилия не может быть пустым")
    private String lastName;
    @NotNull(message = "Поле Дата не может быть пустым")
    @Past(message = "Дата должно быть в прошлом")
    private LocalDate dateOfBirth;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
