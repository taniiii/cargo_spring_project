package com.cargo.exception;

import org.springframework.http.HttpStatus;

public class CargoTranspNotFoundException extends CargoException{

    public CargoTranspNotFoundException(String identifier){
        super(HttpStatus.NOT_FOUND, "Transportation not found for: " + identifier);
    }
}
