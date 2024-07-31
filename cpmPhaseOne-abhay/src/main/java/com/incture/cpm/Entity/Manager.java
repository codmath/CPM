package com.incture.cpm.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="manager")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Manager {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long managerId;

    private Long talentId;

    private String name;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "teamId")
    private Team team;
}
