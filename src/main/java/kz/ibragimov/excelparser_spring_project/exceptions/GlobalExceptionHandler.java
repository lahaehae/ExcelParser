package kz.ibragimov.excelparser_spring_project.exceptions;

import javafx.scene.control.Cell;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc){
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Размер файла слишком велик");
    }
    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<String> handleFieldValidationException(FieldValidationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при вставке данных в поле: " + ex.getFieldName() + "Сообщение: " + ex.getMessage());
    }
    @ExceptionHandler(CellValidationException.class)
    public ResponseEntity<String> handleCellValidationException(CellValidationException ex){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка строки: " + ex.getRowNumber() + "Сообщение: " + ex.getMessage());
    }

}
