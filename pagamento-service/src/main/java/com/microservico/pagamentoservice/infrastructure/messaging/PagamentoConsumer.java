package com.microservico.pagamentoservice.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.microservico.pagamentoservice.application.usecase.CriarPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;

@Component
public class PagamentoConsumer {

    private final CriarPagamento criarPedido;

    public PagamentoConsumer(CriarPagamento criarPedido) {
        this.criarPedido = criarPedido;
    }

    @RabbitListener(queues = "fila.pedidos") // mesma fila usada pelo pedidoreceiver
    public void receberPedido(Pagamento pedido) {
        System.out.println("📩 Pedido recebido do RabbitMQ no PedidoService:");
        System.out.println(pedido);

        // Processar o pedido (salvar, verificar estoque, etc.)
        criarPedido.criarPagamento(pedido);
    }
}
