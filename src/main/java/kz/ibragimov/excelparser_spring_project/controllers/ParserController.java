package kz.ibragimov.excelparser_spring_project.controllers;

import kz.ibragimov.excelparser_spring_project.services.ParserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;


@RestController
@RequestMapping("/api/files")
public class ParserController {

    private final ParserService parserService;

    public ParserController(ParserService parserService){
        this.parserService = parserService;
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
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера: " + ex.getMessage());
        }
    }
}
