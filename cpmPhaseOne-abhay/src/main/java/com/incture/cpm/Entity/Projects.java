package com.incture.cpm.Entity;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="projects")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projects {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;


    private String projectTitle;

    private String projectDescription;

    // @JsonIgnore
    private Blob descriptionFile;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="inkathon_id")
    private Inkathon inkathon;

    @JsonIgnore
    @OneToMany(mappedBy = "projects", cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teamList=new ArrayList<>();
    
}
