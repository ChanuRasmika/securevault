package com.example.securevault.repository;

import com.example.securevault.entity.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void findByOwnerId_ShouldReturnDocumentsForOwner() {
        String ownerId = "user123";
        String otherOwnerId = "user456";
        
        Document doc1 = createTestDocument("doc1.txt", ownerId);
        Document doc2 = createTestDocument("doc2.pdf", ownerId);
        Document doc3 = createTestDocument("doc3.txt", otherOwnerId);
        
        entityManager.persistAndFlush(doc1);
        entityManager.persistAndFlush(doc2);
        entityManager.persistAndFlush(doc3);

        List<Document> result = documentRepository.findByOwnerId(ownerId);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(doc -> doc.getOwnerId().equals(ownerId)));
    }

    @Test
    void findByOwnerId_ShouldReturnEmptyListForNonExistentOwner() {
        String nonExistentOwnerId = "nonexistent";

        List<Document> result = documentRepository.findByOwnerId(nonExistentOwnerId);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_ShouldPersistDocument() {
        Document document = createTestDocument("test.txt", "user123");

        Document saved = documentRepository.save(document);

        assertNotNull(saved.getId());
        assertEquals("test.txt", saved.getName());
        assertEquals("user123", saved.getOwnerId());
    }

    @Test
    void findById_ShouldReturnDocumentWhenExists() {
        Document document = createTestDocument("test.txt", "user123");
        Document saved = entityManager.persistAndFlush(document);

        var result = documentRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("test.txt", result.get().getName());
    }

    private Document createTestDocument(String name, String ownerId) {
        Document doc = new Document();
        doc.setName(name);
        doc.setOwnerId(ownerId);
        doc.setContentType("text/plain");
        doc.setSize(100L);
        doc.setIv("testIV");
        doc.setEncryptedData(new byte[]{1, 2, 3, 4, 5});
        return doc;
    }
}