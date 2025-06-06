package com.microservico.pagamentoservice.application.exception;

public class PagamentoNaoPodeSerEstornadoException extends RuntimeException {
    public PagamentoNaoPodeSerEstornadoException(String message) {
        super(message);
    }
}
