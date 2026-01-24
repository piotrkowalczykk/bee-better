package com.kowal.backend.exception.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ArticleNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public ArticleNotFoundException(String message){
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }
}
