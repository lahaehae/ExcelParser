package kz.ibragimov.excelparser_spring_project.repositories;

import kz.ibragimov.excelparser_spring_project.exceptions.FieldValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DataRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveAll(List<DataRecord> records) {
        String sql = "INSERT INTO data_records (transportcard, iin, first_name, middle_name, last_name, date_of_birth)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        for (DataRecord record : records) {
            try {
                jdbcTemplate.update(sql, record.getTransportCard(), record.getIin(), record.getFirstName(),
                        record.getMiddleName(), record.getLastName(), record.getDateOfBirth());
            } catch (DataAccessException ex){
                if(ex.getMessage().contains("transportcard")) {
                    throw new FieldValidationException("Ошибка в поле 'transportcard': " + ex.getMessage(), "transportcard");
                } else if (ex.getMessage().contains("iin")) {
                    throw new FieldValidationException("Ошибка в поле 'iin': " + ex.getMessage(), "iin");
                } else if (ex.getMessage().contains("first_name")) {
                    throw new FieldValidationException("Ошибка в поле 'first_name': " + ex.getMessage(), "first_name");
                } else if (ex.getMessage().contains("middle_name")) {
                    throw new FieldValidationException("Ошибка в поле 'middle_name': " + ex.getMessage(), "middle_name");
                } else if (ex.getMessage().contains("last_name")) {
                    throw new FieldValidationException("Ошибка в поле 'last_name': " + ex.getMessage(), "last_name");
                } else if (ex.getMessage().contains("date_of_birth")) {
                    throw new FieldValidationException("Ошибка в поле 'date_of_birth': " + ex.getMessage(), "date_of_birth");
                }
            }
            }
            }
        }