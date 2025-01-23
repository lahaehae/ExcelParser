package kz.ibgragimov.excelparcer_spring_project.repository;

import kz.ibgragimov.excelparcer_spring_project.model.DataRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DataRepository {
    private final JdbcTemplate jdbcTemplate;

    public DataRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Transactional
    public void save(DataRecord record){
        String sql = "insert into data_records values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, record.getTransportCard(), record.getIin(),
                record.getFirstName(), record.getMiddleName(), record.getFirstName(), record.getLastName());
    }
}
