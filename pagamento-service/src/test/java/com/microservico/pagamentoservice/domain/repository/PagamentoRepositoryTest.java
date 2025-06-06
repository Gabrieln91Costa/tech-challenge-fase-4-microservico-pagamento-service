package com.microservico.pagamentoservice.domain.repository;

import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Test
    void salvarPagamento_DeveSalvarPagamentoComSucesso() {
        Pagamento pagamento = new Pagamento();
        pagamento.setCpfCliente("12345678901");
        pagamento.setNumeroCartao("1234123412341234");
        pagamento.setValorTotal(100.00);
        pagamento.setStatus(StatusPagamento.ABERTO);

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        assertNotNull(pagamentoSalvo.getId());
        assertEquals("12345678901", pagamentoSalvo.getCpfCliente());
    }

    // Testes para os métodos de busca podem ser adicionados aqui
}
