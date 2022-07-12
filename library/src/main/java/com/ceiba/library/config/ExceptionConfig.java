package com.ceiba.library.config;

import com.ceiba.library.domain.dto.ErrorDto;
import com.ceiba.library.exceptions.CeibaNotAllowedException;
import com.ceiba.library.exceptions.CeibaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionConfig {

    @ExceptionHandler(CeibaNotFoundException.class)
    public ResponseEntity<Object> notFoundException(CeibaNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CeibaNotAllowedException.class)
    public ResponseEntity<Object> notAllowedException(CeibaNotAllowedException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
