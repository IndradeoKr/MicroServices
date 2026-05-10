package com.employee.exception;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {

    @Override
    public HttpStatus deserialize(JsonParser p,
                                  DeserializationContext ctxt)
            throws IOException {

        String value = p.getText();

        // Handles "404 NOT_FOUND"
        if (value.contains(" ")) {
            value = value.substring(value.indexOf(" ") + 1);
        }

        return HttpStatus.valueOf(value);
    }
}