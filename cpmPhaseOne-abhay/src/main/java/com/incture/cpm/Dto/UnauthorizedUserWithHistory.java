package com.incture.cpm.Dto;

import java.util.List;

import com.incture.cpm.Entity.History;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedUserWithHistory {
    private UnauthorizedUserDto user;
    private List<History> history;    

    public UnauthorizedUserWithHistory(UnauthorizedUserDto user, List<History> history) {
        this.user = user;
        this.history = history;
    }
}
