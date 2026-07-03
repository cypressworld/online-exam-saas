package com.exam.saas.controller;

import com.exam.saas.repository.QuestionRepository;
import com.exam.saas.service.ExcelService;
import com.exam.saas.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AppController {

    private final QuestionRepository questionRepository;
    private final ExcelService excelService;
    private final BackupService backupService;

    @Autowired
    public AppController(QuestionRepository questionRepository, ExcelService excelService, BackupService backupService) {
        this.questionRepository = questionRepository;
        this.excelService = excelService;
        this.backupService = backupService;
    }

    @GetMapping("/login")
    public String login() { 
        return "login"; 
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalQuestions", questionRepository.count());
        return "dashboard";
    }

    @GetMapping("/exam/start")
    public String startExam(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "exam-room";
    }

    @PostMapping("/admin/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            excelService.saveExcelQuestions(file);
            backupService.backupQuestionsToExternalStorage(); // Triggers the JSON backup sequence
            model.addAttribute("message", "Questions successfully imported and backed up to external storage!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse template: " + e.getMessage());
        }
        return "dashboard";
    }
}