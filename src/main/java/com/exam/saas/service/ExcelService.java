package com.exam.saas.service;

import com.exam.saas.model.Question;
import com.exam.saas.repository.QuestionRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    
    private final QuestionRepository questionRepository;

    public ExcelService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void saveExcelQuestions(MultipartFile file) throws Exception {
        List<Question> questionList = new ArrayList<>();
        
        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                
                // Safety check: skip completely empty rows
                if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }
                
                Question q = new Question();
                q.setQuestionText(getCellValueSafe(row, 0));
                q.setOptionA(getCellValueSafe(row, 1));
                q.setOptionB(getCellValueSafe(row, 2));
                q.setOptionC(getCellValueSafe(row, 3));
                q.setOptionD(getCellValueSafe(row, 4));
                q.setCorrectAnswer(getCellValueSafe(row, 5).trim().toUpperCase());
                
                questionList.add(q);
            }
            questionRepository.saveAll(questionList);
        }
    }

    // Helper method to protect against null or non-string cell data conversions
    private String getCellValueSafe(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            // Converts numeric values (like numbers or option keys accidentally parsed as numbers) to string safely
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }
}