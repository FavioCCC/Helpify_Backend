package com.webcrafters.helpify.excepciones;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("message")
    @JsonAlias("mensaje")
    private String message;

    private List<String> errors;

    public ErrorResponde(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
