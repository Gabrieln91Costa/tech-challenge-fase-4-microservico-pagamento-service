package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.model.Pagamento;

public interface AtualizarPagamento {
    Pagamento atualizarPagamento(String id, PagamentoRequestDTO pagamentoRequestDTO);
}
