package com.maiphong.springbootmvc.entities;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(50)")
    private String firstName;

    @Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(50)")
    private String lastName;

    @Transient
    private String displayName = firstName + " " + lastName;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
