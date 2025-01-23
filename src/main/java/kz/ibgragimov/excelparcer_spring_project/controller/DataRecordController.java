package kz.ibgragimov.excelparcer_spring_project.controller;

import kz.ibgragimov.excelparcer_spring_project.model.DataRecord;
import kz.ibgragimov.excelparcer_spring_project.repository.DataRepository;
import kz.ibgragimov.excelparcer_spring_project.service.ExcelParsingService;
import kz.ibgragimov.excelparcer_spring_project.service.ValidationService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class DataRecordController {
    private final ExcelParsingService excelParsingService;
    private final ValidationService validationService;
    private final DataRepository dataRepository;

    public DataRecordController(ExcelParsingService excelParsingService, ValidationService validationService,
                                DataRepository dataRepository){
        this.excelParsingService = excelParsingService;
        this.validationService = validationService;
        this.dataRepository = dataRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, ValidationException {
        try {
            List<DataRecord> records = excelParsingService.parseExcelFile(file);

            List<String> errors = validationService.validate(records);
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }
            dataRepository.save((DataRecord) records);
            return ResponseEntity.ok("Файл успешно оброботан и данные сохранены");
        } catch (IOException e){
            return ResponseEntity.status(500).body("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
