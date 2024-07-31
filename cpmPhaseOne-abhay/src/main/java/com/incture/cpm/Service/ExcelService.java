package com.incture.cpm.Service;
import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Repo.CollegeTPORepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private CollegeTPORepo collegeTPORepository;

    public boolean hasExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || contentType.equals("application/vnd.ms-excel"));
    }

    public void save(MultipartFile file) throws IOException {
        List<CollegeTPO> collegeTPOList = excelToCollegeTPOList(file.getInputStream());
        collegeTPORepository.saveAll(collegeTPOList);
    }

    private List<CollegeTPO> excelToCollegeTPOList(InputStream is) {
        List<CollegeTPO> collegeTPOList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                CollegeTPO collegeTPO = new CollegeTPO();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                    case 0 -> collegeTPO.setCollegeName(getCellValueAsString(currentCell));
                    case 1 -> collegeTPO.setTpoName(getCellValueAsString(currentCell));
                    case 2 -> collegeTPO.setPrimaryEmail(getCellValueAsString(currentCell));
                    case 3 -> collegeTPO.setPhoneNumber(getCellValueAsString(currentCell));
                    case 4 -> collegeTPO.setAddressLine1(getCellValueAsString(currentCell));
                    case 5 -> collegeTPO.setAddressLine2(getCellValueAsString(currentCell));
                    case 6 -> collegeTPO.setLocation(getCellValueAsString(currentCell));
                    case 7 -> collegeTPO.setRegion(getCellValueAsString(currentCell));
                    case 8 -> collegeTPO.setCollegeOwner(getCellValueAsString(currentCell));
                    case 9 -> collegeTPO.setPrimaryContact(getCellValueAsString(currentCell));
                    case 10 -> collegeTPO.setSecondaryContact(getCellValueAsString(currentCell));
                    case 11 -> collegeTPO.setTier(getCellValueAsString(currentCell));
                    case 12 -> collegeTPO.setPinCode(getCellValueAsString(currentCell));
                    case 13 -> collegeTPO.setState(getCellValueAsString(currentCell));
                    case 14 -> collegeTPO.setCompensation(getCellValueAsDouble(currentCell));
                
                    }
                    cellIndex++;
                }
                collegeTPOList.add(collegeTPO);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return collegeTPOList;
    }
    // private String getStringCellValue(Cell cell) {
    //     if (cell.getCellType() == CellType.NUMERIC) {
    //         return String.valueOf((int) cell.getNumericCellValue());
    //     } else {
    //         return cell.getStringCellValue();
    //     }
    // }
    // private double getNumericCellValue(Cell cell) {
    //     if (cell.getCellType() == CellType.STRING) {
    //         return Double.parseDouble(cell.getStringCellValue());
    //     } else {
    //         return cell.getNumericCellValue();
    //     }
    // }
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    private double getCellValueAsDouble(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1.0 : 0.0;
            case FORMULA:
                return cell.getNumericCellValue();
            default:
                return 0.0;
        }
    }
    
    
}
