package br.com.claudioescobar.blogsample.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "entity not found")
    public void handleNotFoundException(EmptyResultDataAccessException ex) {
        //all not found resources return an http code 404
    }

}
