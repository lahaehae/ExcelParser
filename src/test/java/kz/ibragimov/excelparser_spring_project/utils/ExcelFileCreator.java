package kz.ibragimov.excelparser_spring_project.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelFileCreator {
    public static MockMultipartFile createMockExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("TransportCard");
        headerRow.createCell(1).setCellValue("IIN");
        headerRow.createCell(2).setCellValue("LastName");
        headerRow.createCell(3).setCellValue("FirstName");
        headerRow.createCell(4).setCellValue("MiddleName");
        headerRow.createCell(5).setCellValue("DateOfBirth");
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(12345);
        dataRow.createCell(1).setCellValue(123456789012L);
        dataRow.createCell(2).setCellValue("Test");
        dataRow.createCell(3).setCellValue("User");
        dataRow.createCell(4).setCellValue("Middle");
        dataRow.createCell(5).setCellValue("01.01.1990");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        byte[] content = outputStream.toByteArray();
        return new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);
    }
}

