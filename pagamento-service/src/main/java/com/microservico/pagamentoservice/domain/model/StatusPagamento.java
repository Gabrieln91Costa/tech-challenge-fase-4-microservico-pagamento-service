package com.microservico.pagamentoservice.domain.model;

public enum StatusPagamento {
    ABERTO,
    FECHADO_COM_SUCESSO,
    FECHADO_SEM_CREDITO,
    FECHADO_SEM_ESTOQUE,
    ESTORNADO,
    CANCELADO
}
