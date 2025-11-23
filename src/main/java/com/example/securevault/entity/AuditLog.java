package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actorId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String targetType;

    private Long targetId;

    private Instant timestamp = Instant.now();

    private String ip;
}

