package kz.ibragimov.excelparser_spring_project.repositories;

import kz.ibragimov.excelparser_spring_project.models.DataRecord;
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
            } catch (Exception e) {
                System.err.println("Ошибка при вставке данных для transportcard: " + record.getTransportCard());
                e.printStackTrace();
            }
        }
    }
}