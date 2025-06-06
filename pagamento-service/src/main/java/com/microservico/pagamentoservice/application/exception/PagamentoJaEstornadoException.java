package com.microservico.pagamentoservice.application.exception;

public class PagamentoJaEstornadoException extends RuntimeException {
    public PagamentoJaEstornadoException(String message) {
        super(message);
    }
}
