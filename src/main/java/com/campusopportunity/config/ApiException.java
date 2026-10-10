package com.campusopportunity.config;

import org.springframework.http.HttpStatus;

// Simple exception we throw from services. The message is shown to the user.
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
