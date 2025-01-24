package kz.ibragimov.excelparser_spring_project.controllers;

import kz.ibragimov.excelparser_spring_project.services.ParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class ParserController {

    private final ParserService parserService;

    public ParserController(ParserService parserService){
        this.parserService = parserService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile (@RequestParam("file")MultipartFile file){
        parserService.parseFile(file);
        return ResponseEntity.ok("Все гуд");
    }
}
