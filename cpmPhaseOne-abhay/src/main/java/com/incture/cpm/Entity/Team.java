package com.incture.cpm.Entity;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    private String teamName;
    private int membersCount;
    private int progress;

    // @JsonIgnore
    private Blob presentationFile;// should be in presentation

    // mapped to inkathon
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "inkathon_id")
    private Inkathon inkathon;

    // mapped to projects
    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects projects;

    // mapped to members
    // @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Member> members = new HashSet<>();

    // @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_mentor", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "mentor_id"))
    Set<Mentor> mentor;

    // @JsonIgnore
    @OneToOne(mappedBy = "team"/* , cascade = CascadeType.ALL */)
    private Manager manager;

}
