package com.microservico.pagamentoservice.domain.dto;

import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoRequestDTOTest {

    private PagamentoRequestDTO dto;
    private ItemRequestDTO item1;
    private ItemRequestDTO item2;
    private List<ItemRequestDTO> itens;

    @BeforeEach
    void setUp() {
        dto = new PagamentoRequestDTO();

        item1 = new ItemRequestDTO();
        item1.setSku("sku-001");
        item1.setQuantidade(2);
        item1.setPrecoUnitario(50.0);

        item2 = new ItemRequestDTO();
        item2.setSku("sku-002");
        item2.setQuantidade(1);
        item2.setPrecoUnitario(100.0);

        itens = Arrays.asList(item1, item2);
    }

    @Test
    void testGettersAndSetters_CpfCliente() {
        dto.setCpfCliente("12345678900");
        assertEquals("12345678900", dto.getCpfCliente());

        dto.setCpfCliente("09876543211");
        assertEquals("09876543211", dto.getCpfCliente());
    }

    @Test
    void testGettersAndSetters_NumeroCartao() {
        dto.setNumeroCartao("1111-2222-3333-4444");
        assertEquals("1111-2222-3333-4444", dto.getNumeroCartao());

        dto.setNumeroCartao("5555-6666-7777-8888");
        assertEquals("5555-6666-7777-8888", dto.getNumeroCartao());
    }

    @Test
    void testGettersAndSetters_ValorTotal() {
        dto.setValorTotal(150.5);
        assertEquals(150.5, dto.getValorTotal());

        dto.setValorTotal(200.0);
        assertEquals(200.0, dto.getValorTotal());
    }

    @Test
    void testGettersAndSetters_Itens() {
        dto.setItens(itens);
        List<ItemRequestDTO> returnedItens = dto.getItens();

        assertNotNull(returnedItens);
        assertEquals(2, returnedItens.size());
        assertEquals("sku-001", returnedItens.get(0).getSku());
        assertEquals("sku-002", returnedItens.get(1).getSku());
    }

    @Test
    void testGettersAndSetters_Status() {
        dto.setStatus(StatusPagamento.ABERTO);
        assertEquals(StatusPagamento.ABERTO, dto.getStatus());

        dto.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        assertEquals(StatusPagamento.FECHADO_COM_SUCESSO, dto.getStatus());
    }
}
