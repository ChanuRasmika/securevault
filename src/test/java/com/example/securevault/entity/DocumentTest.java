package com.example.securevault.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void document_ShouldCreateWithAllFields() {
        String ownerId = "user123";
        String name = "test.txt";
        String contentType = "text/plain";
        Long size = 1024L;
        String iv = "testIV";
        byte[] encryptedData = {1, 2, 3, 4, 5};

        Document document = new Document();
        document.setOwnerId(ownerId);
        document.setName(name);
        document.setContentType(contentType);
        document.setSize(size);
        document.setIv(iv);
        document.setEncryptedData(encryptedData);

        assertEquals(ownerId, document.getOwnerId());
        assertEquals(name, document.getName());
        assertEquals(contentType, document.getContentType());
        assertEquals(size, document.getSize());
        assertEquals(iv, document.getIv());
        assertArrayEquals(encryptedData, document.getEncryptedData());
        assertNotNull(document.getCreatedAt());
    }

    @Test
    void document_ShouldSetCreatedAtOnInstantiation() {
        Instant before = Instant.now();
        Document document = new Document();
        Instant after = Instant.now();

        assertNotNull(document.getCreatedAt());
        assertTrue(document.getCreatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(document.getCreatedAt().isBefore(after.plusSeconds(1)));
    }

    @Test
    void document_ShouldSupportAllArgsConstructor() {
        Long id = 1L;
        String ownerId = "user123";
        String name = "test.txt";
        String contentType = "text/plain";
        Long size = 1024L;
        String iv = "testIV";
        Instant createdAt = Instant.now();
        byte[] encryptedData = {1, 2, 3, 4, 5};

        Document document = new Document(id, ownerId, name, contentType, size, iv, createdAt, encryptedData);

        assertEquals(id, document.getId());
        assertEquals(ownerId, document.getOwnerId());
        assertEquals(name, document.getName());
        assertEquals(contentType, document.getContentType());
        assertEquals(size, document.getSize());
        assertEquals(iv, document.getIv());
        assertEquals(createdAt, document.getCreatedAt());
        assertArrayEquals(encryptedData, document.getEncryptedData());
    }

    @Test
    void document_ShouldSupportNoArgsConstructor() {
        Document document = new Document();
        
        assertNull(document.getId());
        assertNull(document.getOwnerId());
        assertNull(document.getName());
        assertNull(document.getContentType());
        assertNull(document.getSize());
        assertNull(document.getIv());
        assertNotNull(document.getCreatedAt());
        assertNull(document.getEncryptedData());
    }
}