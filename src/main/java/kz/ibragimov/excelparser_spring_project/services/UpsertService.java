package kz.ibragimov.excelparser_spring_project.services;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.repositories.DataRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UpsertService {
    private static final Logger log = LoggerFactory.getLogger(UpsertService.class);
    private static final Marker CELL = MarkerFactory.getMarker("CELL");
    private static final Marker FILE = MarkerFactory.getMarker("FILE");

    private final IdGenerationService idGenerationService;
    private final DataRepository dataRepository;
    private final ValidationService validationService;
    @Autowired
    public UpsertService(DataRepository dataRepository, ValidationService validationService,
                         IdGenerationService idGenerationService){
        this.dataRepository = dataRepository;
        this.validationService = validationService;
        this.idGenerationService = idGenerationService;
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
                    record.setId(idGenerationService.generateId());
                    record.setTransportCard(getLongValue(row.getCell(0), rowIndex));
                    record.setIin(getLongValue(row.getCell(1), rowIndex));
                    record.setLastName(getStringValue(row.getCell(2), rowIndex));
                    record.setFirstName(getStringValue(row.getCell(3), rowIndex));
                    record.setMiddleName(getStringValue(row.getCell(4), rowIndex));
                    record.setDateOfBirth(getDateValue(row.getCell(5), rowIndex));
                    records.add(record);
                    validationService.validateDataRecord(record, rowIndex);
                } catch (CellValidationException ex){
                    log.error(CELL, "Ошибка валидации строк: {}", rowIndex, ex);
                    throw new CellValidationException(ex.getMessage(), rowIndex);
                }
                rowIndex++;
            }
            dataRepository.upsertAll(records);
        } catch (IOException e){
            log.error(FILE, "Ошибка обработки файла: ", e);
            System.err.println("Ошибка обработки файла: " + e.getMessage());
            throw e;
        }
    }
    private LocalDate getDateValue(Cell cell, int rowIndex) {
        if (cell == null) {
            log.error(CELL, "Поле не может быть пустым");
            throw new IllegalArgumentException("Поле не может быть пустым");
        }
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                return LocalDate.parse(cell.getStringCellValue(), formatter);
            } else {
                log.error(CELL, "Неизвестный тип данных для даты в строке: " + rowIndex);
                throw new IllegalArgumentException("Неизвестный тип данных для даты в строке " + rowIndex);
            }
        } catch (Exception e) {
            log.error(CELL, "Ошибка при извлечении даты из ячейки в строке " + rowIndex + ": " + e.getMessage());
            throw new RuntimeException("Ошибка при извлечении даты из ячейки в строке " + rowIndex + ": " + e.getMessage());
        }
    }

    private Long getLongValue(Cell cell, int rowIndex) {
        if (cell == null) {
            log.error(CELL, "Поле не может быть пустым: {}", rowIndex);
            throw new CellValidationException("Поле не может быть пустым" + rowIndex, rowIndex);
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        }
        log.error(CELL, "Не числовое значение в ячейке в строке: {}", rowIndex);
        throw new CellValidationException("Не числовое значение в ячейке в строке " + rowIndex, rowIndex);
    }

    private String getStringValue(Cell cell, int rowIndex) {
        if (cell == null) {
            log.error(CELL,"Поле не может быть пустым: {}", rowIndex);
            throw new CellValidationException("Поле не может быть пустым " + rowIndex, rowIndex);
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        log.error(CELL, "Не строковое значение в ячейке в строке: {}", rowIndex);
        throw new CellValidationException("Не строковое значение в ячейке в строке " + rowIndex , rowIndex);
    }
}

