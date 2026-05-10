package com.address.config;

import com.address.exception.CustomException;
import com.address.exception.ErrorResponse;
import com.address.exception.HttpStatusDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;


public class CustomErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        // adding custom deserializer for custom exception
        SimpleModule module = new SimpleModule();
        module.addDeserializer(HttpStatus.class, new HttpStatusDeserializer());

        objectMapper.registerModule(module);

        objectMapper.findAndRegisterModules();
        try(InputStream is = response.body().asInputStream();){
            ErrorResponse errorResponse = objectMapper.readValue(is, ErrorResponse.class);
            System.out.println(errorResponse.getMessage() + " " + errorResponse.getStatus());

//            JsonNode jsonNode = objectMapper.readTree(is);

//            String message = jsonNode.has("message")
//                    ? jsonNode.get("message").asText()
//                    : "Unknown Error";
//
//            System.out.println(jsonNode.get("status"));
//            System.out.println(response.status());
//            HttpStatus status = jsonNode.has("status")
//                    ? HttpStatus.valueOf(jsonNode.get("status").asText())
//                    : HttpStatus.INTERNAL_SERVER_ERROR;

            return new CustomException(errorResponse.getMessage(), errorResponse.getStatus());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw new CustomException("INTERNAL_SERVER_ERROR");
        }
    }
}
