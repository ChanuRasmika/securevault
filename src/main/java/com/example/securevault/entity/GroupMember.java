package com.example.securevault.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role { OWNER, MEMBER }

}