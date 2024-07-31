package com.incture.cpm.Entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mentor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mentor {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long mentorId;

    private Long talentId;
    private String name;
    private String skills;

    @JsonIgnore
    @ManyToMany(mappedBy = "mentor")
    Set<Team> team;

    

}
