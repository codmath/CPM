package com.incture.cpm.Util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class ExcelUtil {
    public boolean checkExcelFormat(MultipartFile file) {
System.out.println("shi");
        String contentType = file.getContentType();
 
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) return true;
        else return false;
    }
    public List<Map<String, String>> readExcelFile(MultipartFile file) throws IOException {
        System.out.println("mok");
        List<Map<String, String>> dataList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Row headerRow = rowIterator.next();
        List<String> headers = new ArrayList<>();

        for (Cell cell : headerRow) {
            System.out.println("cell");
            headers.add(cell.getStringCellValue());
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, String> data = new HashMap<>();

            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.getCell(i);
                System.out.println(headers.get(i) + " : " + cell.getCellType());
                String cellValue = getCellValueAsString(cell);
                data.put(headers.get(i), cellValue);
            }

            dataList.add(data);
        }

        workbook.close();
        return dataList;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
    public boolean checkExcelHeaders(MultipartFile file, List<String> expectedHeaders) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();

            for (Cell cell : headerRow) headers.add(cell.getStringCellValue());

            workbook.close();

            // Check if the headers match the expected format
            return headers.equals(expectedHeaders);
        }

        workbook.close();
        return false; // If there are no rows, the format is not valid
    }
}
