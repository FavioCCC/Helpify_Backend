package com.webcrafters.helpify.excepciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponde {
    private int statusCode;
    private String message;
    private List<String> errors;

    public ErrorResponde(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
