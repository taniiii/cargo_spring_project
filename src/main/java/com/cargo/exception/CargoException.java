package com.cargo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CargoException extends ResponseStatusException {

    public CargoException(String message) {  //TODO @NonNull
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public CargoException(HttpStatus httpStatus, String message) { //TODO @NonNull
        super(httpStatus, message);
    }

    public CargoException(HttpStatus httpStatus, String message, Throwable cause) {//TODO @NonNull
        super(httpStatus, message, cause);
    }

    @Override
    public String getMessage(){
        return getReason();
    }
}
