package com.incture.cpm.helper;
 
import com.incture.cpm.Entity.Candidate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
 
public class Helper {
 
    // check that file is of excel type or not
    public static boolean checkExcelFormat(MultipartFile file) {
 
        String contentType = file.getContentType();
 
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
 
    }
 
    // convert excel to list of products
 
    public static List<Candidate> convertExcelToListOfProduct(InputStream is) {
        List<Candidate> list = new ArrayList<>();
        Set<String> uniqueEmails = new HashSet<>();
 
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("Sheet1");
 
            Iterator<Row> iterator = sheet.iterator();
 
            while (iterator.hasNext()) {
                Row row = iterator.next();
 
                // Skip header row
                if (row.getRowNum() == 0)
                    continue;
 
                Iterator<Cell> cells = row.cellIterator();
                Candidate candidate = new Candidate();
 
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int columnIndex = cell.getColumnIndex();
 
                    // Set candidate fields based on cell index
                    switch (columnIndex) {
                        case 0:
                            candidate.setCandidateId((long) cell.getNumericCellValue());
                            break;
                        case 1:
                            candidate.setCandidateName(cell.getStringCellValue());
                            break;
                        case 2:
                            candidate.setCandidateCollege(cell.getStringCellValue());
                            break;
                        case 3:
                            candidate.setDepartment(cell.getStringCellValue());
                            break;
                        case 4:
                            candidate.setEmail(cell.getStringCellValue());
                            break;
                        case 5:
                            candidate.setPhoneNumber(String.valueOf((long) cell.getNumericCellValue()));
                            break;
                        case 6:
                            candidate.setAlternateNumber(String.valueOf((long) cell.getNumericCellValue()));
                            break;
                        case 7:
                            candidate.setTenthPercent(cell.getNumericCellValue());
                            break;
                        case 8:
                            candidate.setTwelthPercent(cell.getNumericCellValue());
                            break;
/*                         case 9:
                            candidate.setMarksheetsSemwise(cell.getStringCellValue());
                            break; */
                        case 9:
                            candidate.setCurrentLocation(cell.getStringCellValue());
                            break;
                        case 10:
                            candidate.setPermanentAddress(cell.getStringCellValue());
                            break;
                        case 11:
                            candidate.setPanNumber(cell.getStringCellValue());
                            break;
                        case 12:
                            candidate.setAadhaarNumber(cell.getStringCellValue());
                            break;
                        case 13:
                            candidate.setFatherName(cell.getStringCellValue());
                            break;
                        case 14:
                            candidate.setMotherName(cell.getStringCellValue());
                            break;
                        case 15:
                            candidate.setDOB(cell.getStringCellValue());
                            break;
                        case 16:
                            candidate.setCgpaUndergrad(cell.getNumericCellValue());
                            break;
                        case 17:
                            candidate.setCgpaMasters(cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                }
 
                // Add candidate to the list only if the email is unique
                if (uniqueEmails.add(candidate.getEmail())) {
                    candidate.setStatus("Interview Pending");
                    list.add(candidate);
                }
            }
 
            workbook.close(); // Close workbook to release resources
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return list;
 
    }
 
}