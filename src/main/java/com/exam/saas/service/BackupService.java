package com.exam.saas.service;

import com.exam.saas.model.Question;
import com.exam.saas.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class BackupService {

    private final QuestionRepository questionRepository;

    @Value("${exam.backup.directory}")
    private String backupDir;

    public BackupService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void backupQuestionsToExternalStorage() {
        try {
            File directory = new File(backupDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            File backupFile = new File(directory, "questions_backup.txt");
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(backupFile))) {
                writer.println("=== EXAM QUESTION BANK BACKUP ===");
                for (Question q : questionRepository.findAll()) {
                    writer.println("----------------------------------------");
                    writer.println("ID: " + q.getId());
                    writer.println("Question: " + q.getQuestionText());
                    writer.println("A: " + q.getOptionA());
                    writer.println("B: " + q.getOptionB());
                    writer.println("C: " + q.getOptionC());
                    writer.println("D: " + q.getOptionD());
                    writer.println("Correct: " + q.getCorrectAnswer());
                }
                writer.println("----------------------------------------");
            }
            
            System.out.println("--> External flat-file backup successfully written to: " + backupFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Backup failure encountered: " + e.getMessage());
        }
    }
}