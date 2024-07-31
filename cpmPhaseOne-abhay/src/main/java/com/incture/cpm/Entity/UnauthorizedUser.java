package com.incture.cpm.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
 
@Entity
@Table(name = "UnauthorizedUsers")
@Getter
@Setter

public class UnauthorizedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String inctureId; // INC1234
 
    @Column(nullable = false, unique = true)
    private String email;
 
    @Column(nullable = false)
    private String password;

    private String talentName; // Any name irrespective of talent
    private String status; //pending(get All), approved, declined

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "unauthorised_roles", joinColumns = @JoinColumn(name = "UnauthorizedUser_id"))
    @Column(name = "role")
    private Set<String> roles;

}