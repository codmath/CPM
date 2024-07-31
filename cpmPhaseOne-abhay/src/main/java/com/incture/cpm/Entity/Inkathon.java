package com.incture.cpm.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="inkathon")
public class Inkathon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="inkathon_id")
    Long inkathonId;

    String title;
    LocalDate creationDate=LocalDate.now();
    String description;

    @JsonIgnore
    @OneToMany(mappedBy = "inkathon", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Projects> projectsList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "inkathon", cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teamList=new ArrayList<>();
}
