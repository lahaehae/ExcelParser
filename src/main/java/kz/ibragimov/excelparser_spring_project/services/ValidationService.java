package kz.ibragimov.excelparser_spring_project.services;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);
    private static final Marker CELL = MarkerFactory.getMarker("CELL");

    public void validateDataRecord(DataRecord record, int rowIndex){
        if(record.getTransportCard() == null || record.getTransportCard() < 1){
            log.error(CELL, "Поле 'transportCard' не должно быть пустым: {} ", rowIndex);
            throw new CellValidationException("Поле 'transportCard' не должно быть пустым ", rowIndex);
        }
        if(record.getIin() == null || record.getIin() < 1){
            log.error(CELL, "Поле 'iin' не должно быть пустым: {} ", rowIndex);
            throw new CellValidationException("Поле 'iin' не должно быть пустым ", rowIndex);
        } else if(String.valueOf(record.getIin()).length() != 12){
            log.error(CELL, "Поле 'iin' должно быть длиной в 12 цифр: {} ", rowIndex);
            throw new CellValidationException("Поле 'iin' должно быть длиной в 12 цифр", rowIndex);
        }
        if(record.getFirstName() == null){
            log.error(CELL, "Поле 'firstName' не должно быть пустым: {} ", rowIndex);
            throw new CellValidationException("Поле 'firstName' не должно быть пустым ", rowIndex);
        }
        if(record.getMiddleName() == null){
            log.error(CELL, "Поле 'middleName' не должно быть пустым: {} ", rowIndex);
            throw new CellValidationException("Поле 'middleName' не должно быть пустым ", rowIndex);
        }
        if(record.getLastName() == null){
            log.error(CELL, "Поле 'lastName' не должно быть пустым: {} ", rowIndex);
            throw new CellValidationException("Поле 'lastName' не должно быть пустым ", rowIndex);
        }
        if(record.getDateOfBirth() == null){
            log.error(CELL, "Поле 'dateOfBirth' не должно быть пустым: {} ", rowIndex);
            throw new CellValidationException("Поле 'dateOfBirth' не должно быть пустым", rowIndex);
        } else if (record.getDateOfBirth().isAfter(LocalDate.now())) {
            log.error(CELL, "Поле 'dateOfBirth' не должно быть в будущем: {}", rowIndex);
            throw new CellValidationException("Поле 'dateOfBirth' не должно быть в будущем", rowIndex);
        }
    }
}
