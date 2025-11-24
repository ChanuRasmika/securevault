package com.example.securevault.controller;


import com.example.securevault.entity.Document;
import com.example.securevault.service.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) {
        String userId = principal.getName();
        try {
            service.upload(file, userId);
            redirectAttributes.addFlashAttribute("uploadSuccess", "File uploaded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("uploadError", e.getMessage() != null ? e.getMessage() : "Upload failed.");
        }
        return "redirect:/dashboard";
    }


    @GetMapping
    public String list(Model model, Principal principal) {
        String userId = principal.getName();
        List<Document> docs = service.listMyDocuments(userId);
        model.addAttribute("documents", docs);
        return "documents";
    }


    @GetMapping("/{id}")
    @ResponseBody
    public Document get(@PathVariable Long id, Principal principal) {
        String userId = principal.getName();
        return service.getMetadata(id, userId);
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long id, Principal principal) throws Exception {
        String userId = principal.getName();
        Document doc = service.getMetadata(id, userId);
        byte[] decrypted = service.getDecryptedData(id, userId);

        ByteArrayResource resource = new ByteArrayResource(decrypted);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .contentLength(decrypted.length)
                .body(resource);
    }
}
