package com.incture.cpm.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Evaluator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluatorId;//update, delete by this
    private String employeeId;
    private String evaluatorName;
    private String skills;
    private  String location;
    private String email;
    private String designation;
    @Transient
    public List<String> getSkillsList() {
        return skills != null ? Arrays.asList(skills.split(",")) : null;
    }

    @Transient
    public void setSkillsList(List<String> skillsList) {
        this.skills = skillsList != null ? String.join(",", skillsList) : null;
    }
}