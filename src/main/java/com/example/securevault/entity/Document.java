package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "documents")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Document {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String iv;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    @Basic(fetch = FetchType.LAZY)
    private byte[] encryptedData;

}