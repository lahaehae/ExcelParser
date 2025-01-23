package kz.ibgragimov.excelparcer_spring_project.service;

import kz.ibgragimov.excelparcer_spring_project.model.DataRecord;
import kz.ibgragimov.excelparcer_spring_project.repository.DataRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelParsingService{
    private final ValidationService validationService;
    private final DataRepository dataRepository;

    public ExcelParsingService(ValidationService validationService, DataRepository dataRepository){
        this.validationService = validationService;
        this.dataRepository = dataRepository;
    }

    public List<DataRecord> parseExcelFile(MultipartFile file) throws IOException{
        List<DataRecord> records = new ArrayList<>();
        try(Workbook workbook = new XSSFWorkbook(file.getInputStream())){
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                DataRecord record = new DataRecord();
                record.setTransportCard((long) row.getCell(0).getNumericCellValue());
                record.setIin((long) row.getCell(1).getNumericCellValue());
                record.setFirstName(row.getCell(2).getStringCellValue());
                record.setMiddleName(row.getCell(3).getStringCellValue());
                record.setLastName(row.getCell(4).getStringCellValue());
                record.setDateOfBirth(row.getCell(5).getLocalDateTimeCellValue().toLocalDate());

                records.add(record);

            }
        }
        return records;
    }
}
