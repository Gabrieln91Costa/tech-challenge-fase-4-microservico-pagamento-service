package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriarPagamentoTest {

    // Implementação simples para teste
    static class CriarPagamentoImpl implements CriarPagamento {

        @Override
        public Pagamento criarPagamento(Pagamento pagamento) {
            // Exemplo simples: seta status "ABERTO" e retorna o pagamento
            pagamento.setStatus(null); // só para mostrar que pode setar algo
            return pagamento;
        }
    }

    @Test
    void deveCriarPagamentoComSucesso() {
        CriarPagamento criarPagamento = new CriarPagamentoImpl();

        Pagamento pagamento = new Pagamento();
        pagamento.setId("pag001");
        pagamento.setCpfCliente("12345678900");

        ItemPagamento item = new ItemPagamento();
        item.setSku("sku123");
        item.setQuantidade(2);
        pagamento.getItens().add(item);

        Pagamento resultado = criarPagamento.criarPagamento(pagamento);

        assertNotNull(resultado);
        assertEquals("pag001", resultado.getId());
        assertEquals("12345678900", resultado.getCpfCliente());
        assertEquals(1, resultado.getItens().size());
        assertEquals("sku123", resultado.getItens().get(0).getSku());
    }
}
