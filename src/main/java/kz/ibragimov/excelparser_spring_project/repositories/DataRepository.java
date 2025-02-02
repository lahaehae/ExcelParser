package kz.ibragimov.excelparser_spring_project.repositories;

import kz.ibragimov.excelparser_spring_project.exceptions.FieldValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.rowmappers.DataRowmapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Comparator;
import java.util.List;

@Repository
public class DataRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Transactional
    public void saveAll(List<DataRecord> records) {
        String sql = "INSERT INTO data_records (id, transportcard, iin, first_name, middle_name, last_name, date_of_birth)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        for (DataRecord record : records) {
            try {
                jdbcTemplate.update(sql, record.getId(), record.getTransportCard(), record.getIin(), record.getFirstName(),
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

//    public boolean updateOrInsertAll(List<DataRecord> records){
//        String checkSql = "SELECT COUNT(*) FROM  data_records WHERE iin = ?";
//        Integer count = jdbcTemplate.queryForObject()
//
//    }
//    @Transactional
//    public void upsertAll(List<DataRecord> records) {
//        records.sort(Comparator.comparing(DataRecord::getIin));
//        for (DataRecord record : records) {
//            try {
//                String checkSql = "SELECT * FROM data_records WHERE iin=?";
//                jdbcTemplate.queryForObject(checkSql, new DataRowmapper(), record.getIin());
//                String updateSql = "UPDATE data_records SET  transportcard=?, first_name=?, middle_name=?, " +
//                        "last_name=?, date_of_birth=? WHERE iin=?";
//                jdbcTemplate.update(updateSql, record.getTransportCard(), record.getFirstName(),
//                        record.getMiddleName(), record.getLastName(), record.getDateOfBirth(), record.getIin());
//            } catch (EmptyResultDataAccessException ex) {
//                String insertSql = "INSERT INTO data_records (id, transportcard, iin, last_name, first_name, middle_name, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
//                jdbcTemplate.update(insertSql, record.getId(), record.getTransportCard(), record.getIin(), record.getFirstName(),
//                        record.getMiddleName(), record.getLastName(), record.getDateOfBirth());
//            }
//        }
//    }

//    @Transactional
//    public void upsertAll(List<DataRecord> records) {
//        records.sort(Comparator.comparing(DataRecord::getIin));
//        for (DataRecord record : records) {
//            try{
//                String checkSql = "SELECT * FROM data_records WHERE iin=?";
//                jdbcTemplate.queryForObject(checkSql, new DataRowmapper(), record.getIin());
//                String updateSql = "UPDATE data_records SET  transportcard=?, first_name=?, middle_name=?, " +
//                        "last_name=?, date_of_birth=? WHERE iin=?";
//                jdbcTemplate.update(updateSql, record.getTransportCard(), record.getFirstName(),
//                        record.getMiddleName(), record.getLastName(), record.getDateOfBirth(), record.getIin());
//            } catch(EmptyResultDataAccessException ex){
//                String insertSql = "INSERT INTO data_records (id, transportcard, iin, last_name, first_name, middle_name, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
//                jdbcTemplate.update(insertSql, record.getId(), record.getTransportCard(), record.getIin(), record.getFirstName(),
//                        record.getMiddleName(), record.getLastName(), record.getDateOfBirth());
//            }
//        }
//        }
    @Transactional
    public void upsertAll(List<DataRecord> records){
        records.sort(Comparator.comparing(DataRecord::getIin));
        for(DataRecord record : records){
            int index = binarySearch(records, record.getIin());
            if(index != -1){
                    String checkSql = "SELECT * FROM data_records WHERE iin=?";
                    jdbcTemplate.queryForObject(checkSql, new DataRowmapper(), record.getIin());
                    String updateSql = "UPDATE data_records SET transportcard=?, first_name=?, middle_name=?, " +
                            "last_name=?, date_of_birth=? WHERE iin=?";
                    jdbcTemplate.update(updateSql, record.getTransportCard(), record.getFirstName(),
                            record.getMiddleName(), record.getLastName(), record.getDateOfBirth(), record.getIin());
            } else {
                String insertSql = "INSERT INTO data_records (id, transportcard, iin, last_name, first_name, middle_name, date_of_birth) VALUES (?,?,?,?,?,?,?)";
                jdbcTemplate.update(insertSql, record.getId(), record.getTransportCard(), record.getIin(), record.getLastName(),
                        record.getFirstName(), record.getMiddleName(), record.getDateOfBirth());
            }


        }
    }

    public void updateById(Long id, DataRecord updatedRecord){
        String sql = "UPDATE data_records SET transportcard=?, iin=?, first_name=?, middle_name=?, " +
                "last_name=?, date_of_birth=? WHERE id=?";
        try {
            jdbcTemplate.update(sql, updatedRecord.getTransportCard(), updatedRecord.getIin(),
                    updatedRecord.getFirstName(), updatedRecord.getMiddleName(),
                    updatedRecord.getLastName(), updatedRecord.getDateOfBirth(), id);
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
        public DataRecord findById(Long id){
            return jdbcTemplate.queryForObject("SELECT * FROM data_records WHERE id=?", new DataRowmapper(), id);
        }

        public int binarySearch(List<DataRecord> records, Long targetIin){
        int left = 0;
        int right = records.size() - 1;
        while(left<=right){
            int mid = left + (right - left) / 2;
            Long midIin = records.get(mid).getIin();

            if(midIin.equals(targetIin)){
                return mid;
            }else if(midIin < targetIin){
                left = mid + 1;
            } else{
                right = mid - 1;
            }
        }
        return -1;


        }

    }