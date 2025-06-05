package com.microservico.pagamentoservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPagamentoTest {

    @Test
    void deveConterTodosOsStatusEsperados() {
        assertEquals(6, StatusPagamento.values().length);

        assertNotNull(StatusPagamento.valueOf("ABERTO"));
        assertNotNull(StatusPagamento.valueOf("FECHADO_COM_SUCESSO"));
        assertNotNull(StatusPagamento.valueOf("FECHADO_SEM_CREDITO"));
        assertNotNull(StatusPagamento.valueOf("FECHADO_SEM_ESTOQUE"));
        assertNotNull(StatusPagamento.valueOf("ESTORNADO"));
        assertNotNull(StatusPagamento.valueOf("CANCELADO"));
    }

    @Test
    void deveConverterDeStringParaEnumCorretamente() {
        StatusPagamento status = StatusPagamento.valueOf("ABERTO");
        assertEquals(StatusPagamento.ABERTO, status);
    }

    @Test
    void deveLancarExcecaoParaValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatusPagamento.valueOf("INEXISTENTE");
        });
    }
}
