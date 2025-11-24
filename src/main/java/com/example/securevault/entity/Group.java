package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`groups`")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;
}