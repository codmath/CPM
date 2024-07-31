package com.incture.cpm.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private Long talentId;
    private Set<String> roles;
    private String talentName;
    private String inctureId;
    private byte[] photo;
}
