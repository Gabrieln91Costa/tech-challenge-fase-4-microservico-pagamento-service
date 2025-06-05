package com.microservico.pagamentoservice.domain.exception;

public class PagNaoEncontradoException extends RuntimeException {
    public PagNaoEncontradoException(String id) {
        super("Pagamento não encontrado para o ID: " + id);
    }
}
