package com.cargo.exception;

import org.springframework.http.HttpStatus;

/**
 * This exception is thrown when user not found in database.
 */
public class CargoUserNotFoundException extends CargoException{

    public CargoUserNotFoundException(String userCredentials) {
        super(HttpStatus.NOT_FOUND, "User not found: " + userCredentials);
    }
}
