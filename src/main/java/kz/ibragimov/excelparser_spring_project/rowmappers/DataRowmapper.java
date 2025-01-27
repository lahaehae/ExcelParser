package kz.ibragimov.excelparser_spring_project.rowmappers;

import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataRowmapper implements RowMapper<DataRecord> {
    public DataRecord mapRow(ResultSet rs, int rowNum ) throws SQLException{
        DataRecord record = new DataRecord();
        record.setId(rs.getLong("id"));
        record.setTransportCard(rs.getLong("transportcard"));
        record.setIin(rs.getLong("iin"));
        record.setFirstName(rs.getString("first_name"));
        record.setMiddleName(rs.getString("middle_name"));
        record.setLastName(rs.getString("last_name"));
        record.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return record;
    }
}
