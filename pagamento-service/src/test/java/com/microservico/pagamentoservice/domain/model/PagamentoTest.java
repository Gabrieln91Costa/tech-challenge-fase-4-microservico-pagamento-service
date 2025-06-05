package com.microservico.pagamentoservice.domain.model;

import java.util.Arrays;
import java.util.List;

public class PagamentoTest {

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
}
