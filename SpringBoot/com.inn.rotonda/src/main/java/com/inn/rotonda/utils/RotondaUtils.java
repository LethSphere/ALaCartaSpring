package com.inn.rotonda.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RotondaUtils {

    private RotondaUtils(){

    }
    
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"valio brga\"}",httpStatus);
    }
    
}
