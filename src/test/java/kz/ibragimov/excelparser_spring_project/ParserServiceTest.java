package kz.ibragimov.excelparser_spring_project;

import kz.ibragimov.excelparser_spring_project.exceptions.CellValidationException;
import kz.ibragimov.excelparser_spring_project.models.DataRecord;
import kz.ibragimov.excelparser_spring_project.repositories.DataRepository;
import kz.ibragimov.excelparser_spring_project.services.IdGenerationService;
import kz.ibragimov.excelparser_spring_project.services.ParserService;
import kz.ibragimov.excelparser_spring_project.services.ValidationService;
import kz.ibragimov.excelparser_spring_project.utils.ExcelFileCreator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParserServiceTest {
    @InjectMocks
    private ParserService parserService;
    @Mock
    private DataRepository dataRepository;
    @Mock
    private ValidationService validationService;
    @Mock
    private IdGenerationService idGenerationService;
    @Test
    void testParseFile_validFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("test-data.xlsx");
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test-data.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                fileContent
        );
        when(idGenerationService.generateId()).thenReturn(1L);
        parserService.parseFile(mockFile);
//        verify(dataRepository, times(1)).saveAll(anyList());
        verify(dataRepository, times(1)).saveAll(anyList());
        ArgumentCaptor<List<DataRecord>> captor = ArgumentCaptor.forClass(List.class);
        verify(dataRepository).saveAll(captor.capture());
        List<DataRecord> savedRecord = captor.getValue();
        assertFalse(savedRecord.isEmpty());
        assertEquals(1L, savedRecord.get(0).getId());

    }
//    @Test
//    void testParseFile_invalidDateFormat() throws IOException {
//        ClassPathResource resource = new ClassPathResource("test-data.xlsx");
//        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
//        MockMultipartFile mockFile = new MockMultipartFile(
//                "file", "test-data.xlsx",
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//                fileContent
//        );
//            assertThrows(RuntimeException.class, () -> parserService.parseFile(mockFile));
//    }
//    @Test
//    void testGetLongValue_invalidType() {
//        // Создаём мок объекта Cell (поддельную ячейку Excel)
//        Cell mockCell = mock(Cell.class);
//
//        // Говорим, что mockCell.getCellType() должен вернуть STRING (а не число)
//        when(mockCell.getCellType()).thenReturn(CellType.STRING);
//
//        // Говорим, что mockCell.getStringCellValue() вернёт "non-numeric" (строку)
//        when(mockCell.getStringCellValue()).thenReturn("non-numeric");
//
//        // Проверяем, что вызов метода getLongValue() вызовет исключение
//        assertThrows(CellValidationException.class, () -> parserService.getLongValue(mockCell, 1));
//    }
//
//    @Test
//    void testGetStringValue_invalidType(){
//        Cell mockCell = mock(Cell.class);
//        lenient().when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
//        lenient().when(mockCell.getNumericCellValue()).thenReturn(12345.0);
//        assertThrows(CellValidationException.class, () -> parserService.getStringValue(mockCell, 1));
//    }
}