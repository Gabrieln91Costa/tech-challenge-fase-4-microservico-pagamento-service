package com.microservico.pagamentoservice.domain.repository;

import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @BeforeEach
    void setup() {
        // Limpa a coleção antes de cada teste para evitar dados residuais
        pagamentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar e recuperar pagamento por CPF do cliente")
    void deveBuscarPagamentoPorCpfCliente() {
        Pagamento pagamento = new Pagamento();
        pagamento.setCpfCliente("12345678900");
        pagamento.setNumeroCartao("1111-2222-3333-4444");
        pagamento.setValorTotal(150.0);
        pagamento.setStatus(StatusPagamento.ABERTO);
        pagamento.setItens(Arrays.asList()); // inicializa lista para evitar NPE

        pagamentoRepository.save(pagamento);

        List<Pagamento> resultado = pagamentoRepository.findByCpfCliente("12345678900");

        assertEquals(1, resultado.size());
        assertEquals("12345678900", resultado.get(0).getCpfCliente());
    }

    @Test
    @DisplayName("Deve buscar pagamento por status")
    void deveBuscarPagamentoPorStatus() {
        Pagamento pagamento = new Pagamento();
        pagamento.setCpfCliente("00000000000");
        pagamento.setNumeroCartao("9999-8888-7777-6666");
        pagamento.setValorTotal(200.0);
        pagamento.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        pagamento.setItens(Arrays.asList()); // inicializa lista

        pagamentoRepository.save(pagamento);

        List<Pagamento> resultado = pagamentoRepository.findByStatus(StatusPagamento.FECHADO_COM_SUCESSO);

        assertFalse(resultado.isEmpty());
        assertEquals(StatusPagamento.FECHADO_COM_SUCESSO, resultado.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve buscar pagamento por SKU de item")
    void deveBuscarPagamentoPorSku() {
        ItemPagamento item = new ItemPagamento("sku001", 2, 25.0);

        Pagamento pagamento = new Pagamento();
        pagamento.setCpfCliente("11111111111");
        pagamento.setNumeroCartao("1234-5678-9012-3456");
        pagamento.setValorTotal(50.0);
        pagamento.setStatus(StatusPagamento.ABERTO);
        pagamento.setItens(Arrays.asList(item)); // inicializa lista com o item

        pagamentoRepository.save(pagamento);

        List<Pagamento> resultado = pagamentoRepository.findBySku("sku001");

        assertEquals(1, resultado.size());
        assertEquals("sku001", resultado.get(0).getItens().get(0).getSku());
    }
}
