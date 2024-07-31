package com.incture.cpm.helper;

import java.util.ArrayList;

import com.incture.cpm.Dto.TalentAssessmentDto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class AssessmentHelper {

    public static List<TalentAssessmentDto> convertExcelToAssessmentRecord(InputStream inpStream) {
        List<TalentAssessmentDto> list = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inpStream);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (row.getRowNum() == 0)
                    continue;

                Iterator<Cell> cells = row.cellIterator();
                TalentAssessmentDto assessmentDto = new TalentAssessmentDto();

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            assessmentDto.setTalentId((long) cell.getNumericCellValue());
                            break;

                        case 1:
                            assessmentDto.setAssessmentId((long) cell.getNumericCellValue());
                            break;

                        case 2:
                            assessmentDto.setAssessmentDate(cell.getStringCellValue());
                            break;

                        case 3:
                            assessmentDto.setAssessmentType(cell.getStringCellValue());
                            break;

                        case 4:
                            assessmentDto.setAssessmentSkill(cell.getStringCellValue());
                            break;

                        case 5:
                            assessmentDto.setLocation(String.valueOf(cell.getStringCellValue()));
                            break;

                        case 6:
                            double score = cell.getNumericCellValue();
                            assessmentDto.setScore(score);
                            break;

                        default:
                            break;
                    }
                }
                list.add(assessmentDto);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}