package com.eareiza.springAngular.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandler {

    @Autowired
    private MessageSource mensajes;

    StringBuilder errorMessage = new StringBuilder();

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> exception(HttpServletRequest request, DataIntegrityViolationException exception) {
        // return error info object with standard json
        ErrorInfo errorInfo = new ErrorInfo(mensajes.getMessage("error.exception.dataIntegrityViolation",
                null, null),
                HttpStatus.CONFLICT.value(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AperturaCajaException.class)
    public ResponseEntity<Object> AperturaCajaException(HttpServletRequest request, AperturaCajaException exception) {
        // return error info object with standard json
        ErrorInfo errorInfo = new ErrorInfo( mensajes.getMessage("error.caja.aperturaCaja",
                null, null ),
                HttpStatus.CONFLICT.value(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public  ResponseEntity<Object> DataAccessException(HttpServletRequest request, NoSuchElementException exception){
        // return error info object with standard json
        ErrorInfo errorInfo = new ErrorInfo(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {

        // get spring errors
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        // convert errors to standard string
        StringBuilder errorMessage = new StringBuilder();
        fieldErrors.forEach(f -> errorMessage.append(f.getField() + " " + f.getDefaultMessage() +  " "));

        // return error info object with standard json
        ErrorInfo errorInfo = new ErrorInfo(errorMessage.toString(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

}
