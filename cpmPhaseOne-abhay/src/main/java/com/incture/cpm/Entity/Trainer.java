package com.incture.cpm.Entity;
 
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
 
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Trainer {
    @Id
    String trainerId;//inctureid       update, delete by this
    String trainerName;
    String[] skills;
    String location;
    String email;
    String designation;
}