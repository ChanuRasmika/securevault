package com.example.securevault.service;

import com.example.securevault.entity.Document;
import com.example.securevault.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private final String testSecretKey = Base64.getEncoder().encodeToString(new byte[32]);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(documentService, "secretKeyBase64", testSecretKey);
    }

    @Test
    void upload_ShouldEncryptAndSaveDocument() throws Exception {
        String content = "test file content";
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", content.getBytes()
        );
        String ownerId = "user123";
        
        Document savedDocument = new Document();
        savedDocument.setId(1L);
        savedDocument.setOwnerId(ownerId);
        savedDocument.setName("test.txt");
        
        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);

        Document result = documentService.upload(file, ownerId);

        assertNotNull(result);
        assertEquals(ownerId, result.getOwnerId());
        assertEquals("test.txt", result.getName());
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void listMyDocuments_ShouldReturnUserDocuments() {
        String ownerId = "user123";
        List<Document> expectedDocs = Arrays.asList(
                createTestDocument(1L, "doc1.txt", ownerId),
                createTestDocument(2L, "doc2.pdf", ownerId)
        );
        
        when(documentRepository.findByOwnerId(ownerId)).thenReturn(expectedDocs);

        List<Document> result = documentService.listMyDocuments(ownerId);

        assertEquals(2, result.size());
        assertEquals(expectedDocs, result);
        verify(documentRepository).findByOwnerId(ownerId);
    }

    @Test
    void getMetadata_ShouldReturnDocumentForOwner() {
        Long docId = 1L;
        String userId = "user123";
        Document document = createTestDocument(docId, "test.txt", userId);
        
        when(documentRepository.findById(docId)).thenReturn(Optional.of(document));

        Document result = documentService.getMetadata(docId, userId);

        assertEquals(document, result);
        verify(documentRepository).findById(docId);
    }

    @Test
    void getMetadata_ShouldThrowExceptionForNonOwner() {
        Long docId = 1L;
        String userId = "user123";
        String differentUserId = "user456";
        Document document = createTestDocument(docId, "test.txt", differentUserId);
        
        when(documentRepository.findById(docId)).thenReturn(Optional.of(document));

        assertThrows(RuntimeException.class, () -> 
            documentService.getMetadata(docId, userId)
        );
    }

    private Document createTestDocument(Long id, String name, String ownerId) {
        Document doc = new Document();
        doc.setId(id);
        doc.setName(name);
        doc.setOwnerId(ownerId);
        doc.setContentType("text/plain");
        doc.setSize(100L);
        doc.setIv(Base64.getEncoder().encodeToString(new byte[12]));
        doc.setEncryptedData(new byte[]{1, 2, 3, 4, 5});
        return doc;
    }
}