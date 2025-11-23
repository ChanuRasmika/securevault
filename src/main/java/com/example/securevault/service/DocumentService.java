package com.example.securevault.service;

import com.example.securevault.entity.Document;
import com.example.securevault.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Value("${encryption.secret}")
    private String secretKeyBase64;  // 256-bit key in Base64

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    //Upload + encrypt
    public Document upload(MultipartFile file, String ownerId) throws Exception {
        String originalName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        // Generate a random 12-byte IV
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        String ivBase64 = Base64.getEncoder().encodeToString(iv);

        // Encrypt file stream → byte[] using the generated IV
        byte[] encryptedBytes = encrypt(file.getInputStream(), iv);

        Document doc = new Document();
        doc.setOwnerId(ownerId);
        doc.setName(originalName);
        doc.setContentType(contentType);
        doc.setSize(size);
        doc.setIv(ivBase64);
        doc.setEncryptedData(encryptedBytes);

        return documentRepository.save(doc);
    }


    public List<Document> listMyDocuments(String ownerId) {
        return documentRepository.findByOwnerId(ownerId);
    }


    public Document getMetadata(Long id, String userId) {
        Document doc = documentRepository.findById(id).orElseThrow();
        if (!doc.getOwnerId().equals(userId)) throw new RuntimeException("Access denied");
        return doc;
    }


    public byte[] getDecryptedData(Long id, String userId) throws Exception {
        Document doc = getMetadata(id, userId);
        return decrypt(doc.getEncryptedData(), doc.getIv());
    }


    private SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Use the provided IV for encryption. Do NOT reuse the same IV for another encryption with the same key.
    private byte[] encrypt(InputStream plainStream, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKey key = getKey();
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = plainStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) encryptedOut.write(output);
        }
        byte[] finalOutput = cipher.doFinal();
        if (finalOutput != null) encryptedOut.write(finalOutput);

        return encryptedOut.toByteArray();
    }

    private byte[] decrypt(byte[] encryptedData, String ivBase64) throws Exception {
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(128, iv));
        return cipher.doFinal(encryptedData);
    }
}
