package com.example.securevault.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "is_group_admin", nullable = false)
    private Boolean isGroupAdmin = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public Boolean getIsGroupAdmin() { return isGroupAdmin; }
    public void setIsGroupAdmin(Boolean isGroupAdmin) { this.isGroupAdmin = isGroupAdmin; }
}