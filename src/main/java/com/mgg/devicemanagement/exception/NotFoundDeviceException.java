package com.mgg.devicemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundDeviceException extends RuntimeException {
    public NotFoundDeviceException(String message) {
        super(message);
    }
}
