package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.dto.ItemRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AtualizarPagamentoTest {

    // Implementação de exemplo para teste
    static class AtualizarPagamentoImpl implements AtualizarPagamento {

        @Override
        public Pagamento atualizarPagamento(String id, PagamentoRequestDTO dto) {
            Pagamento p = new Pagamento();
            p.setId(id);
            p.setCpfCliente(dto.getCpfCliente());
            p.setNumeroCartao(dto.getNumeroCartao());
            p.setValorTotal(dto.getValorTotal());

            // Transforma itens DTO em ItemPagamento
            if (dto.getItens() != null) {
                for (ItemRequestDTO itemDTO : dto.getItens()) {
                    ItemPagamento item = new ItemPagamento();
                    item.setSku(itemDTO.getSku());
                    item.setQuantidade(itemDTO.getQuantidade());
                    item.setPrecoUnitario(itemDTO.getPrecoUnitario());
                    p.getItens().add(item);
                }
            }
            return p;
        }
    }

    @Test
    void deveAtualizarPagamentoCorretamente() {
        AtualizarPagamento atualizarPagamento = new AtualizarPagamentoImpl();

        PagamentoRequestDTO dto = new PagamentoRequestDTO();
        dto.setCpfCliente("12345678900");
        dto.setNumeroCartao("1234123412341234");
        dto.setValorTotal(250.0);

        ItemRequestDTO itemDTO = new ItemRequestDTO();
        itemDTO.setSku("sku123");
        itemDTO.setQuantidade(2);
        itemDTO.setPrecoUnitario(125.0);
        dto.setItens(Collections.singletonList(itemDTO));

        Pagamento pagamento = atualizarPagamento.atualizarPagamento("id001", dto);

        assertEquals("id001", pagamento.getId());
        assertEquals("12345678900", pagamento.getCpfCliente());
        assertEquals("1234123412341234", pagamento.getNumeroCartao());
        assertEquals(250.0, pagamento.getValorTotal());
        assertEquals(1, pagamento.getItens().size());
        assertEquals("sku123", pagamento.getItens().get(0).getSku());
        assertEquals(2, pagamento.getItens().get(0).getQuantidade());
        assertEquals(125.0, pagamento.getItens().get(0).getPrecoUnitario());
    }

    @Test
    void deveRetornarPagamentoVazioQuandoDtoSemItens() {
        AtualizarPagamento atualizarPagamento = new AtualizarPagamentoImpl();

        PagamentoRequestDTO dto = new PagamentoRequestDTO();
        dto.setCpfCliente("00000000000");
        dto.setNumeroCartao("0000000000000000");
        dto.setValorTotal(0.0);
        dto.setItens(null);

        Pagamento pagamento = atualizarPagamento.atualizarPagamento("id002", dto);

        assertEquals("id002", pagamento.getId());
        assertEquals("00000000000", pagamento.getCpfCliente());
        assertEquals("0000000000000000", pagamento.getNumeroCartao());
        assertEquals(0.0, pagamento.getValorTotal());
        assertTrue(pagamento.getItens().isEmpty());
    }
}
