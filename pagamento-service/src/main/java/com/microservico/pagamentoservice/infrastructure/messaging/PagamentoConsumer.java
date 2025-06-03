package com.microservico.pagamentoservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservico.pagamentoservice.application.usecase.CriarPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConsumer {

    private final CriarPagamento criarPedido;
    private final ObjectMapper objectMapper = new ObjectMapper(); // usado para exibir JSON bonito

    public PagamentoConsumer(CriarPagamento criarPedido) {
        this.criarPedido = criarPedido;
    }

    @RabbitListener(queues = "fila.pedidos")
    public void receberPedido(Pagamento pedido) {
        System.out.println("📩 Pedido de Pagamento recebido do RabbitMQ do PedidoService:");

        try {
            String jsonFormatado = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pedido);
            System.out.println(jsonFormatado); // saída em JSON bonitinho
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao exibir pagamento como JSON. Exibindo toString:");
            System.out.println(pedido); // fallback
        }

        // criarPedido.criarPagamento(pedido);
    }
}
