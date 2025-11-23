package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "access_requests")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    private Long requesterId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 500)
    private String reason;

    private Instant createdAt = Instant.now();

    public enum Status { PENDING, APPROVED, DENIED }
}
