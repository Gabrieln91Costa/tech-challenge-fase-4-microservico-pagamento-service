package com.microservico.pagamentoservice.api.exception;

import com.microservico.pagamentoservice.domain.exception.PagNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PagNaoEncontradoException.class)
    public ResponseEntity<Void> handlePagamentoNaoEncontrado(PagNaoEncontradoException ex) {
        return ResponseEntity.notFound().build();
    }
}
