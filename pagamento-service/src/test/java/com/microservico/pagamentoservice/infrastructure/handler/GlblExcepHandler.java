package com.microservico.pagamentoservice.infrastructure.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservico.pagamentoservice.domain.exception.PagNaoEncontradoException;

@ControllerAdvice
public class GlblExcepHandler {

    @ExceptionHandler(PagNaoEncontradoException.class)
    public ResponseEntity<String> handleEstoqueNaoEncontrado(PagNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Pode adicionar outros handlers de exceção aqui
}
