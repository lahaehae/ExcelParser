package kz.ibragimov.excelparser_spring_project.controllers;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.exceptions.FieldValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.repositories.DataRepository;
import kz.ibragimov.excelparser_spring_project.services.ParserService;
import kz.ibragimov.excelparser_spring_project.services.UpsertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;


@RestController
@RequestMapping("/api/files")
public class ParserController {
    private static final Logger log = LoggerFactory.getLogger(ParserController.class);

    private final ParserService parserService;
    private final DataRepository dataRepository;
    private final UpsertService upsertService;

    public ParserController(ParserService parserService, DataRepository dataRepository,
                            UpsertService upsertService){
        this.parserService = parserService;
        this.dataRepository = dataRepository;
        this.upsertService = upsertService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile (@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            log.error("Пустой файл {}", file.getName());
            return ResponseEntity.badRequest().body("Файл не может быть пустым");
        }
        String fileName = file.getOriginalFilename();
        if(fileName != null && !fileName.endsWith(".xlsx")){
            log.error("Формат файла не правильный", fileName);
            return ResponseEntity.badRequest().body("Неверный формат файла. Ожидается .xlsx");
        }
        try{
        parserService.parseFile(file);
        log.info("Файл успешно спарсирован" + file);
        return ResponseEntity.ok("Все гуд");
        } catch(ValidationException ex){
            log.error("Ошибка валидации: " + ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка валидации: " + ex.getMessage());
        } catch(CellValidationException ex){
            log.error("Ошибка в строке: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка в строке: " + ex.getMessage());
        } catch (IOException ex){
            log.error("Ошибка обработки файла: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки файла: " + ex.getMessage());
        } catch (Exception ex){
            log.error("Внутренняя ошибка сервера: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера: " + ex.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody DataRecord updatedRecord, @PathVariable("id") Long id){
        try {
            dataRepository.updateById(id, updatedRecord);
            log.info("Запись с id: {} успешно обновлена ", id, updatedRecord);
            return ResponseEntity.ok("Все гуччи");
        } catch(FieldValidationException ex){
            log.error("Ошибка валидации данных: ", ex);
            throw ex;
        } catch (Exception e) {
            log.error("Ошибка при обновленнии записи ", e);
            throw new RuntimeException("Ошибка при обновленнии записи",e);
        }
    }

    @GetMapping("/{id}")
    public DataRecord findById(@PathVariable("id") Long id){
        try{
        log.info("Получен запрос для метода findById с id={}", id);
        return dataRepository.findById(id);} catch(EmptyResultDataAccessException ex){
            log.error("Данных с таким id нет", ex);
            throw new EmptyResultDataAccessException("Данных с таким id нет", ex.getActualSize());
        }
    }

    @PostMapping("/upsert")
    public ResponseEntity<String> upsertFile (@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            log.error("Файл пустой");
            return ResponseEntity.badRequest().body("Файл не может быть пустым");
        }
        String fileName = file.getOriginalFilename();
        if(fileName != null && !fileName.endsWith(".xlsx")){
            log.error("Неверный формат файла.");
            return ResponseEntity.badRequest().body("Неверный формат файла. Ожидается .xlsx");
        }
        try{
            upsertService.parseFile(file);
            log.info("Файл успешно спарсен");
            return ResponseEntity.ok("Все гуд");
        } catch(ValidationException ex){
            log.error("Ошбика валидации: ", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка валидации: " + ex.getMessage());
        } catch(CellValidationException ex){
            log.error("Ошибка в строке: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка в строке: " + ex.getMessage());
        } catch (IOException ex){
            log.error("Ошибка обработки файла: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки файла: " + ex.getMessage());
        } catch (Exception ex){
            log.error("Внутренняя ошибка сервера: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера: " + ex.getMessage());
        }
    }
}
