package com.chunkusers.chunking_rest.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    String message;
    public UserNotFoundException(String mess){
        message = mess;
    }
}
