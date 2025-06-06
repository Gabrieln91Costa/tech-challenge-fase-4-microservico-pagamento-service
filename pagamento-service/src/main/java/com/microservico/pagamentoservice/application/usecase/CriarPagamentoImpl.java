package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;

public class CriarPagamentoImpl implements CriarPagamento {

    private final PagamentoRepository pagamentoRepository;

    public CriarPagamentoImpl(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Pagamento criarPagamento(Pagamento pagamento) {
        // Aqui você pode adicionar regras de negócio antes de salvar, se precisar
        return pagamentoRepository.save(pagamento);
    }
}
