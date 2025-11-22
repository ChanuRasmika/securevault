package com.example.securevault.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "docs")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;
    
    @Column(name = "user_id")
    private Long userId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String document;

    public Long getDocId() { return docId; }
    public void setDocId(Long docId) { this.docId = docId; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
}