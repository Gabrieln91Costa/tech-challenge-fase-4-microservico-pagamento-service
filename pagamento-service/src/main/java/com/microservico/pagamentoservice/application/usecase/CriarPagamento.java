package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.model.Pagamento;

public interface CriarPagamento {
    
    Pagamento criarPagamento(Pagamento pagamento);

}
