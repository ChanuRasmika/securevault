package com.example.securevault.repository;

import com.example.securevault.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByOwnerId(String ownerId);
}
