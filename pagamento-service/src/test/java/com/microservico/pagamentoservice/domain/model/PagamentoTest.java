package com.microservico.pagamentoservice.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {

    // Método auxiliar para criar um pagamento de teste
    public static Pagamento criarPagamentoTeste() {
        // Criar itens de pagamento (exemplo)
        ItemPagamento item1 = new ItemPagamento("Produto A", 2, 50.0);
        ItemPagamento item2 = new ItemPagamento("Produto B", 1, 100.0);
        List<ItemPagamento> itens = Arrays.asList(item1, item2);

        // Calcular valor total para garantir consistência
        Double valorTotal = itens.stream()
                                 .mapToDouble(i -> i.getQuantidade() * i.getPrecoUnitario())
                                 .sum();

        // Criar o pagamento teste
        Pagamento pagamentoTeste = new Pagamento();
        pagamentoTeste.setId("123456");
        pagamentoTeste.setCpfCliente("123.456.789-00");
        pagamentoTeste.setNumeroCartao("4111111111111111");
        pagamentoTeste.setValorTotal(valorTotal);
        pagamentoTeste.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        pagamentoTeste.setItens(itens);

        return pagamentoTeste;
    }

    @Test
    void testEqualsSameObject() {
        // Testa se o objeto é igual a si mesmo (verifica o "this == o")
        Pagamento pagamento = criarPagamentoTeste();
        assertEquals(pagamento, pagamento);  // O objeto deve ser igual a si mesmo
    }

    @Test
    void testEqualsDifferentType() {
        // Testa se o pagamento não é igual a um objeto de tipo diferente (verifica o "!(o instanceof Pagamento)")
        Pagamento pagamento = criarPagamentoTeste();
        String outroObjeto = "Não é um pagamento";
        assertNotEquals(outroObjeto, pagamento);  // O pagamento não deve ser igual a um objeto de tipo diferente
    }

    @Test
    void testEquals() {
        // Testa se dois objetos Pagamento com os mesmos valores são iguais
        Pagamento pagamento1 = criarPagamentoTeste();
        Pagamento pagamento2 = criarPagamentoTeste();
        assertEquals(pagamento1, pagamento2);  // Dois objetos com os mesmos valores devem ser iguais
    }

    @Test
    void testEqualsDifferentId() {
        // Testa se objetos com IDs diferentes não são iguais
        Pagamento pagamento1 = criarPagamentoTeste();
        Pagamento pagamento2 = criarPagamentoTeste();
        pagamento2.setId("654321");  // Modifica o ID
        assertNotEquals(pagamento1, pagamento2);  // Os objetos com IDs diferentes devem ser diferentes
    }

    @Test
    void testEqualsNull() {
        // Testa se o pagamento não é igual a null
        Pagamento pagamento = criarPagamentoTeste();
        assertNotEquals(null, pagamento);  // O pagamento não deve ser igual a null
    }

    @Test
    void testEqualsDifferentStatus() {
        // Testa se objetos com status diferentes não são iguais
        Pagamento pagamento1 = criarPagamentoTeste();
        Pagamento pagamento2 = criarPagamentoTeste();
        pagamento2.setStatus(StatusPagamento.ABERTO);  // Modifica o status
        assertNotEquals(pagamento1, pagamento2);  // Os objetos com status diferentes devem ser diferentes
    }

    @Test
    void testEqualsDifferentItens() {
        // Testa se objetos com listas de itens diferentes não são iguais
        Pagamento pagamento1 = criarPagamentoTeste();
        Pagamento pagamento2 = criarPagamentoTeste();
        ItemPagamento item3 = new ItemPagamento("Produto C", 1, 150.0);
        pagamento2.setItens(Arrays.asList(item3));  // Modifica a lista de itens
        assertNotEquals(pagamento1, pagamento2);  // Os objetos com itens diferentes devem ser diferentes
    }
}
