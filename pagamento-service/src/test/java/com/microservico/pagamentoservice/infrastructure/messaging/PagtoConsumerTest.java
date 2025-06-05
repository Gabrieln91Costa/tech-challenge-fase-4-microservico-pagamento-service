package com.microservico.pagamentoservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservico.pagamentoservice.application.usecase.CriarPagamento;
import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class PagtoConsumerTest {

    private CriarPagamento criarPagamentoMock;
    private PagamentoConsumer pagamentoConsumer;

    @BeforeEach
    void setUp() {
        criarPagamentoMock = mock(CriarPagamento.class);
        pagamentoConsumer = new PagamentoConsumer(criarPagamentoMock);
    }

    @Test
    void deveReceberPedidoEExibirJsonFormatado() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId("pag123");
        pagamento.setCpfCliente("12345678900");
        pagamento.setNumeroCartao("1111-2222-3333-4444");
        pagamento.setValorTotal(99.90);
        pagamento.setStatus(StatusPagamento.ABERTO);
        pagamento.setItens(Collections.singletonList(
                new ItemPagamento("sku123", 1, 99.90)
        ));

        pagamentoConsumer.receberPedido(pagamento);

        // Não há assertivas, pois o método apenas imprime. 
        // Se quiser, pode redirecionar o System.out para testar logs.
    }

    @Test
    void deveLidarComExcecaoAoSerializarJson() throws Exception {
        ObjectMapper mapperMock = mock(ObjectMapper.class);
        Pagamento pagamento = new Pagamento();
        pagamento.setId("pag001");

        // Cria uma instância real mas substitui o ObjectMapper via reflexão
        PagamentoConsumer consumer = new PagamentoConsumer(criarPagamentoMock);

        // Simula falha ao serializar o pagamento
        ObjectMapper realMapper = new ObjectMapper() {
            @Override
            public String writeValueAsString(Object value) throws JsonProcessingException {
                throw new JsonProcessingException("Erro simulado") {};
            }
        };

        // Reflete o mapper privado (não recomendado em produção, mas ok para testes)
        var field = PagamentoConsumer.class.getDeclaredField("objectMapper");
        field.setAccessible(true);
        field.set(consumer, realMapper);

        consumer.receberPedido(pagamento);
    }
}
