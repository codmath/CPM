package com.incture.cpm.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apiresponse {
    private String message;
    
    public Apiresponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
