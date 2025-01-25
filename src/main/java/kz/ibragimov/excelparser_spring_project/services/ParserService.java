package kz.ibragimov.excelparser_spring_project.services;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.repositories.DataRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ParserService {
    private final DataRepository dataRepository;

    public ParserService(DataRepository dataRepository){
        this.dataRepository = dataRepository;
    }

    public void parseFile(MultipartFile file) throws IOException, CellValidationException {
        try(InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream)){

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            List<DataRecord> records = new ArrayList<>();

            rowIterator.next();
            int rowIndex = 2;

            while(rowIterator.hasNext()){
                Row row = rowIterator.next();

                DataRecord record = new DataRecord();
                try {
                    record.setTransportCard(getLongValue(row.getCell(0), rowIndex));
                    record.setIin(getLongValue(row.getCell(1), rowIndex));
                    record.setLastName(getStringValue(row.getCell(2), rowIndex));
                    record.setFirstName(getStringValue(row.getCell(3), rowIndex));
                    record.setMiddleName(getStringValue(row.getCell(4), rowIndex));
                    record.setDateOfBirth(getDateValue(row.getCell(5), rowIndex));
                    records.add(record);
                } catch (CellValidationException ex){
                    throw new CellValidationException(ex.getMessage(), rowIndex);
                }
                rowIndex++;
            }
            dataRepository.saveAll(records);
        } catch (IOException e){
            System.err.println("Ошибка обработки файла: " + e.getMessage());
            throw e;
        }
    }
    private LocalDate getDateValue(Cell cell, int rowIndex) {
        if (cell == null) {
            throw new IllegalArgumentException("Поле не может быть пустым");
        }
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                return LocalDate.parse(cell.getStringCellValue(), formatter);
            } else {
                throw new IllegalArgumentException("Неизвестный тип данных для даты в строке " + rowIndex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при извлечении даты из ячейки в строке " + rowIndex + ": " + e.getMessage());
        }
    }

    private Long getLongValue(Cell cell, int rowIndex) {
        if (cell == null) {
            throw new CellValidationException("Поле не может быть пустым" + rowIndex, rowIndex);
        }
            if (cell.getCellType() == CellType.NUMERIC) {
                return (long) cell.getNumericCellValue();
            }
            throw new CellValidationException("Не числовое значение в ячейке в строке " + rowIndex, rowIndex);
        }

    private String getStringValue(Cell cell, int rowIndex) {
        if (cell == null) {
            throw new CellValidationException("Поле не может быть пустым " + rowIndex, rowIndex);
        }
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            }
            throw new CellValidationException("Не строковое значение в ячейке в строке " + rowIndex , rowIndex);
        }
}

