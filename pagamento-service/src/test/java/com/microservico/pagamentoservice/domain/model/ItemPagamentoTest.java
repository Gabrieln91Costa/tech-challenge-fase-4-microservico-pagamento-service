package com.microservico.pagamentoservice.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPagamentoTest {

    @Test
    void deveCriarItemPagamentoComGettersESetters() {
        ItemPagamento item = new ItemPagamento();
        item.setSku("SKU-001");
        item.setQuantidade(10);
        item.setPrecoUnitario(15.50);

        assertEquals("SKU-001", item.getSku());
        assertEquals(10, item.getQuantidade());
        assertEquals(15.50, item.getPrecoUnitario());
    }

    @Test
    void deveCriarItemPagamentoComConstrutorCompleto() {
        ItemPagamento item = new ItemPagamento("SKU-002", 5, 20.0);

        assertEquals("SKU-002", item.getSku());
        assertEquals(5, item.getQuantidade());
        assertEquals(20.0, item.getPrecoUnitario());
    }

    @Test
    void devePermitirAlteracaoDosCampos() {
        ItemPagamento item = new ItemPagamento("SKU-003", 3, 7.5);
        item.setSku("SKU-004");
        item.setQuantidade(8);
        item.setPrecoUnitario(10.0);

        assertEquals("SKU-004", item.getSku());
        assertEquals(8, item.getQuantidade());
        assertEquals(10.0, item.getPrecoUnitario());
    }

    @Test
    void deveValidarEqualsEHashCode() {
        ItemPagamento item1 = new ItemPagamento("SKU-005", 2, 30.0);
        ItemPagamento item2 = new ItemPagamento("SKU-005", 2, 30.0);
        ItemPagamento item3 = new ItemPagamento("SKU-006", 2, 30.0);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        assertNotEquals(item1, item3);
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }

    @Test
    void deveRetornarStringCorreta() {
        ItemPagamento item = new ItemPagamento("SKU-007", 4, 12.5);
        String esperado = "ItemPagamento{sku='SKU-007', quantidade=4, precoUnitario=12.5}";

        assertEquals(esperado, item.toString());
    }
}
