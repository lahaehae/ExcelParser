package kz.ibragimov.excelparser_spring_project.controllers;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.exceptions.FieldValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.repositories.DataRepository;
import kz.ibragimov.excelparser_spring_project.services.ParserService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;


@RestController
@RequestMapping("/api/files")
public class ParserController {

    private final ParserService parserService;
    private final DataRepository dataRepository;

    public ParserController(ParserService parserService, DataRepository dataRepository){
        this.parserService = parserService;
        this.dataRepository = dataRepository;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile (@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("Файл не может быть пустым");
        }
        String fileName = file.getOriginalFilename();
        if(fileName != null && !fileName.endsWith(".xlsx")){
            return ResponseEntity.badRequest().body("Неверный формат файла. Ожидается .xlsx");
        }
        try{
        parserService.parseFile(file);
        return ResponseEntity.ok("Все гуд");
        } catch(ValidationException ex){
            return ResponseEntity.badRequest().body("Ошибка валидации: " + ex.getMessage());
        } catch(CellValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка в строке: " + ex.getMessage());
        } catch (IOException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки файла: " + ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера: " + ex.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody DataRecord updatedRecord, @PathVariable("id") Long id){
        try {
            dataRepository.updateById(id, updatedRecord);
            return ResponseEntity.ok("Все гуччи");
        } catch(FieldValidationException ex){
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновленнии записи",e);
        }
    }

    @GetMapping("/{id}")
    public DataRecord findById(@PathVariable("id") Long id){
        return dataRepository.findById(id);
    }
}
