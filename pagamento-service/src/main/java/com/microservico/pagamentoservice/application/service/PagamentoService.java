package com.microservico.pagamentoservice.application.service;

import com.microservico.pagamentoservice.application.usecase.AtualizarPagamento;
import com.microservico.pagamentoservice.application.usecase.BuscarPagamento;
import com.microservico.pagamentoservice.application.usecase.CriarPagamento;
import com.microservico.pagamentoservice.domain.dto.ItemRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRetornoDTO;
import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService implements CriarPagamento, BuscarPagamento, AtualizarPagamento {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String estoqueServiceUrl = "http://estoqueservice:8080/estoque";

    @Override
    public Pagamento criarPagamento(Pagamento pagamento) {
        for (ItemPagamento item : pagamento.getItens()) {
            ResponseEntity<Void> estoqueResponse = restTemplate.getForEntity(
                    estoqueServiceUrl + "/" + item.getSku(), Void.class
            );

            if (estoqueResponse.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("Produto com SKU " + item.getSku() + " não encontrado no estoque.");
            }
        }

        pagamento.setStatus(StatusPagamento.ABERTO);

        for (ItemPagamento item : pagamento.getItens()) {
            String urlBaixar = estoqueServiceUrl + "/" + item.getSku() + "/baixar?quantidade=" + item.getQuantidade();
            restTemplate.put(urlBaixar, null);
        }

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento processarPagamento(PagamentoRequestDTO dto) {
        Pagamento pagamento = new Pagamento();
        pagamento.setCpfCliente(dto.getCpfCliente());
        pagamento.setNumeroCartao(dto.getNumeroCartao());
        pagamento.setValorTotal(dto.getValorTotal());

        List<ItemPagamento> itens = new ArrayList<>();
        for (ItemRequestDTO itemDTO : dto.getItens()) {
            ItemPagamento item = new ItemPagamento();
            item.setSku(itemDTO.getSku());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itens.add(item);
        }

        pagamento.setItens(itens);
        pagamento.setStatus(StatusPagamento.ABERTO);

        return criarPagamento(pagamento);
    }

    public Pagamento processarRetornoPagamento(PagamentoRetornoDTO retornoDTO) {
        Pagamento pagamento = pagamentoRepository.findById(retornoDTO.getPagamentoId())
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if ("SUCESSO".equalsIgnoreCase(retornoDTO.getStatus())) {
            pagamento.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        } else {
            // Estorna estoque se necessário
            for (ItemPagamento item : pagamento.getItens()) {
                String urlRepor = estoqueServiceUrl + "/" + item.getSku() + "/repor";
                restTemplate.put(urlRepor, null);
            }
            pagamento.setStatus(StatusPagamento.FECHADO_SEM_CREDITO);
        }

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento estornarPagamento(String id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (pagamento.getStatus() == StatusPagamento.FECHADO_COM_SUCESSO) {
            for (ItemPagamento item : pagamento.getItens()) {
                String urlRepor = estoqueServiceUrl + "/" + item.getSku() + "/repor";
                restTemplate.put(urlRepor, null);
            }
            pagamento.setStatus(StatusPagamento.ESTORNADO);
        }

        return pagamentoRepository.save(pagamento);
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

    @Override
    public Pagamento atualizarPagamento(String id, PagamentoRequestDTO dto) {
        Pagamento pagamentoExistente = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamentoExistente.setCpfCliente(dto.getCpfCliente());
        pagamentoExistente.setNumeroCartao(dto.getNumeroCartao());
        pagamentoExistente.setValorTotal(dto.getValorTotal());

        List<ItemPagamento> itensAtualizados = new ArrayList<>();
        for (ItemRequestDTO itemDTO : dto.getItens()) {
            ItemPagamento item = new ItemPagamento();
            item.setSku(itemDTO.getSku());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itensAtualizados.add(item);
        }
        pagamentoExistente.setItens(itensAtualizados);

        return pagamentoRepository.save(pagamentoExistente);
    }
}
