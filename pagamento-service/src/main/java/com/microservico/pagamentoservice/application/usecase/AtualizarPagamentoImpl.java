package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.application.exception.PagamentoNaoEncontradoException;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;

public class AtualizarPagamentoImpl implements AtualizarPagamento {

    private final PagamentoRepository pagamentoRepository;

    public AtualizarPagamentoImpl(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Pagamento atualizarPagamento(String id, PagamentoRequestDTO pagamentoRequestDTO) {
        Pagamento pagamentoExistente = pagamentoRepository.findById(id)
                .orElseThrow(() -> new PagamentoNaoEncontradoException("Pagamento não encontrado"));

        pagamentoExistente.setCpfCliente(pagamentoRequestDTO.getCpfCliente());
        pagamentoExistente.setNumeroCartao(pagamentoRequestDTO.getNumeroCartao());
        pagamentoExistente.setValorTotal(pagamentoRequestDTO.getValorTotal());

        return pagamentoRepository.save(pagamentoExistente);
    }
}
