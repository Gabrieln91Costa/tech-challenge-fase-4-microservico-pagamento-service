package com.microservico.pagamentoservice.api.exception;

import com.microservico.pagamentoservice.domain.exception.PagamentoNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PagamentoNaoEncontradoException.class)
    public ResponseEntity<Void> handlePagamentoNaoEncontrado(PagamentoNaoEncontradoException ex) {
        return ResponseEntity.notFound().build();
    }
}
