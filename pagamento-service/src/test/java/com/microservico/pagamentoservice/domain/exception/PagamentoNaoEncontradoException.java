package com.microservico.pagamentoservice.domain.exception;

public class PagamentoNaoEncontradoException extends RuntimeException {
    public PagamentoNaoEncontradoException(String id) {
        super("Pagamento não encontrado para o ID: " + id);
    }
}
