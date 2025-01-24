package kz.ibragimov.excelparser_spring_project.services;

import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.repositories.DataRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ParserService {
    private final DataRepository dataRepository;

    public ParserService(DataRepository dataRepository){
        this.dataRepository = dataRepository;
    }

    public void parseFile(MultipartFile file){
        try(InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream)){

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            List<DataRecord> records = new ArrayList<>();

            rowIterator.next();

            while(rowIterator.hasNext()){
                Row row = rowIterator.next();

                DataRecord record = new DataRecord();

                record.setTransportCard((long) row.getCell(0).getNumericCellValue());
                record.setIin((long) row.getCell(1).getNumericCellValue());
                record.setFirstName(row.getCell(2).getStringCellValue());
                record.setMiddleName(row.getCell(3).getStringCellValue());
                record.setLastName(row.getCell(4).getStringCellValue());
                record.setDateOfBirth(row.getCell(5).getStringCellValue());
                records.add(record);
            }
            dataRepository.saveAll(records);
        } catch (Exception e){
            throw new RuntimeException("Ошибка обработки файла: " + e.getMessage());
        }
    }
    }
