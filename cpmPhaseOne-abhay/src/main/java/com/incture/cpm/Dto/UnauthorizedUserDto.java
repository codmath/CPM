package com.incture.cpm.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UnauthorizedUserDto {
    private Long id;
    private String email;
    private Set<String> roles;
    private String talentName;
    private String inctureId;
    public String status;
}
