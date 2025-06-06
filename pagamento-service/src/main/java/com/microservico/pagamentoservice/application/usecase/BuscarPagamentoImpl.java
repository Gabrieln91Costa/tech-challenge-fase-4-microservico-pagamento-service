package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;

import java.util.List;
import java.util.Optional;

public class BuscarPagamentoImpl implements BuscarPagamento {

    private final PagamentoRepository pagamentoRepository;

    public BuscarPagamentoImpl(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Optional<Pagamento> porId(String id) {
        return pagamentoRepository.findById(id);
    }

    @Override
    public List<Pagamento> porSku(String sku) {
        return pagamentoRepository.findBySku(sku);
    }

    @Override
    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAll();
    }
}
