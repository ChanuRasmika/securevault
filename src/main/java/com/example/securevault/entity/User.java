package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(nullable = false, unique = true)
    private String sub;

    @Column(unique = true, nullable = false)
    private String email;

}