package com.example.securevault.controller;

import com.example.securevault.entity.Document;
import com.example.securevault.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MyFilesController {

    private final DocumentService documentService;

    public MyFilesController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/my-files")
    public String myFiles(Model model, Principal principal) {
        String userId = principal.getName();
        List<Document> docs = documentService.listMyDocuments(userId);
        model.addAttribute("documents", docs);
        model.addAttribute("username", userId);
        model.addAttribute("activePage", "my-files");
        return "my-files";
    }
}

