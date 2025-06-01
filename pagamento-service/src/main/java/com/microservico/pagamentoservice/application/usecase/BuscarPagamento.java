package com.microservico.pagamentoservice.application.usecase;

import java.util.List;
import java.util.Optional;

import com.microservico.pagamentoservice.domain.model.Pagamento;

public interface BuscarPagamento {

    /**
     * Busca um pagamento pelo seu ID único.
     */
    Optional<Pagamento> porId(String id);

    /**
     * Busca todos os pagamentos que contenham um item com o SKU informado.
     */
    List<Pagamento> porSku(String sku);

    /**
     * Retorna todos os pagamentos existentes.
     */
    List<Pagamento> listarPagamentos();
}
