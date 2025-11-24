package com.example.securevault.controller;

import com.example.securevault.entity.Document;
import com.example.securevault.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
@SpringJUnitConfig
@ActiveProfiles("test")
class DocumentControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DocumentService documentService() {
            return mock(DocumentService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentService documentService;

    @Test
    @WithMockUser(username = "user123")
    void upload_ShouldRedirectToDashboardOnSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "test content".getBytes()
        );
        
        Document savedDoc = new Document();
        savedDoc.setId(1L);
        savedDoc.setName("test.txt");
        
        when(documentService.upload(any(), eq("user123"))).thenReturn(savedDoc);

        mockMvc.perform(multipart("/documents")
                .file(file)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(username = "user123")
    void upload_ShouldRedirectWithErrorOnException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "test content".getBytes()
        );
        
        when(documentService.upload(any(), eq("user123")))
                .thenThrow(new RuntimeException("Upload failed"));

        mockMvc.perform(multipart("/documents")
                .file(file)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(username = "user123")
    void list_ShouldReturnDocumentsView() throws Exception {
        List<Document> documents = Arrays.asList(
                createTestDocument(1L, "doc1.txt"),
                createTestDocument(2L, "doc2.pdf")
        );
        
        when(documentService.listMyDocuments("user123")).thenReturn(documents);

        mockMvc.perform(get("/documents"))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"))
                .andExpect(model().attribute("documents", documents));
    }

    @Test
    @WithMockUser(username = "user123")
    void get_ShouldReturnDocumentMetadata() throws Exception {
        Document document = createTestDocument(1L, "test.txt");
        
        when(documentService.getMetadata(1L, "user123")).thenReturn(document);

        mockMvc.perform(get("/documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test.txt"));
    }

    @Test
    @WithMockUser(username = "user123")
    void download_ShouldReturnFileContent() throws Exception {
        Document document = createTestDocument(1L, "test.txt");
        byte[] fileContent = "test file content".getBytes();
        
        when(documentService.getMetadata(1L, "user123")).thenReturn(document);
        when(documentService.getDecryptedData(1L, "user123")).thenReturn(fileContent);

        mockMvc.perform(get("/documents/1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
                .andExpect(content().contentType("text/plain"))
                .andExpect(content().bytes(fileContent));
    }

    private Document createTestDocument(Long id, String name) {
        Document doc = new Document();
        doc.setId(id);
        doc.setName(name);
        doc.setOwnerId("user123");
        doc.setContentType("text/plain");
        doc.setSize(100L);
        return doc;
    }
}