package kz.ibragimov.excelparser_spring_project.services;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ValidationService {

    public void validateDataRecord(DataRecord record, int rowIndex){
        if(record.getTransportCard() == null || record.getTransportCard() < 1){
            throw new CellValidationException("Поле 'transportCard' не должно быть пустым ", rowIndex);
        }
        if(record.getIin() == null || record.getIin() < 1){
            throw new CellValidationException("Поле 'iin' не должно быть пустым ", rowIndex);
        } else if(String.valueOf(record.getIin()).length() != 12){
            throw new CellValidationException("Поле 'iin' должно быть длиной в 12 цифр", rowIndex);
        }
        if(record.getFirstName() == null){
            throw new CellValidationException("Поле 'firstName' не должно быть пустым ", rowIndex);
        }
        if(record.getMiddleName() == null){
            throw new CellValidationException("Поле 'middleName' не должно быть пустым ", rowIndex);
        }
        if(record.getLastName() == null){
            throw new CellValidationException("Поле 'lastName' не должно быть пустым ", rowIndex);
        }
        if(record.getDateOfBirth() == null){
            throw new CellValidationException("Поле 'dateOfBirth' не должно быть пустым", rowIndex);
        } else if (record.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new CellValidationException("Поле 'dateOfBirth' не должно быть в будущем", rowIndex);
        }


    }
}
