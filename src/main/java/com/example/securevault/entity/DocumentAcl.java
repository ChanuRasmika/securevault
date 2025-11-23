package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_acl")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentAcl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    private String subjectId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum SubjectType { USER, GROUP }
    public enum Role { OWNER, EDITOR, VIEWER }
}

