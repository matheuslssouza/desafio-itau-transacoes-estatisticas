package com.itau.desafio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgument(IllegalArgumentException ex) {
        // Regras de negócio violadas (ex: data no futuro ou transação > 60s) -> 422
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidation(MethodArgumentNotValidException ex) {
        // Erros de campos obrigatórios (@NotNull) ou validação de formato -> 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> handleNotReadable(HttpMessageNotReadableException ex) {
        // JSON malformado -> 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
