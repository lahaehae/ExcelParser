package kz.ibgragimov.excelparcer_spring_project.service;

import kz.ibgragimov.excelparcer_spring_project.model.DataRecord;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService {
    public List<String> validate(List<DataRecord> records) throws ValidationException {
        List<String> errors = new ArrayList<>();
        int rowIndex = 1;
        for(DataRecord record : records) {
            if (record.getFirstName().isEmpty()) {
                throw new ValidationException("First name cannot be empty");
            }
            if (record.getLastName().isEmpty()) {
                throw new ValidationException("Last name cannot be empty");
            }
            if (record.getIin() <= 0) {
                throw new ValidationException("IIN cannot be empty");
            }
            if (record.getTransportCard() <= 0) {
                throw new ValidationException("TransportCard cannot be equal to or less than 0");
            }
            rowIndex++;
        }
        return errors;
    }
}
