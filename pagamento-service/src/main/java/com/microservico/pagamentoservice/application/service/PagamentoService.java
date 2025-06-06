package com.microservico.pagamentoservice.application.service;

import com.microservico.pagamentoservice.application.exception.PagamentoJaEstornadoException;
import com.microservico.pagamentoservice.application.exception.PagamentoNaoEncontradoException;
import com.microservico.pagamentoservice.application.exception.PagamentoNaoPodeSerEstornadoException;
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

    private static final String PAGAMENTO_NAO_ENCONTRADO = "Pagamento não encontrado";
    private static final String PAGAMENTO_JA_ESTORNADO = "Pagamento já foi estornado";
    private static final String PAGAMENTO_NAO_PODE_SER_ESTORNADO = "Pagamento não pode ser estornado";
    private static final String PRODUTO_NAO_ENCONTRADO = "Produto com SKU %s não encontrado no estoque.";

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
                throw new PagamentoNaoEncontradoException(
                    String.format(PRODUTO_NAO_ENCONTRADO, item.getSku())
                );
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
                .orElseThrow(() -> new PagamentoNaoEncontradoException(PAGAMENTO_NAO_ENCONTRADO));

        if ("SUCESSO".equalsIgnoreCase(retornoDTO.getStatus())) {
            pagamento.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        } else {
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
                .orElseThrow(() -> new PagamentoNaoEncontradoException(PAGAMENTO_NAO_ENCONTRADO));

        if (pagamento.getStatus() == StatusPagamento.ESTORNADO) {
            throw new PagamentoJaEstornadoException(PAGAMENTO_JA_ESTORNADO);
        }
        if (pagamento.getStatus() != StatusPagamento.FECHADO_COM_SUCESSO &&
            pagamento.getStatus() != StatusPagamento.FECHADO_SEM_CREDITO) {
            throw new PagamentoNaoPodeSerEstornadoException(PAGAMENTO_NAO_PODE_SER_ESTORNADO);
        }

        for (ItemPagamento item : pagamento.getItens()) {
            String urlRepor = estoqueServiceUrl + "/" + item.getSku() + "/repor";
            restTemplate.put(urlRepor, null);
        }
        pagamento.setStatus(StatusPagamento.ESTORNADO);
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
    public Pagamento atualizarPagamento(String id, PagamentoRequestDTO pagamentoRequestDTO) {
        Pagamento pagamentoExistente = pagamentoRepository.findById(id)
                .orElseThrow(() -> new PagamentoNaoEncontradoException(PAGAMENTO_NAO_ENCONTRADO));

        pagamentoExistente.setCpfCliente(pagamentoRequestDTO.getCpfCliente());
        pagamentoExistente.setNumeroCartao(pagamentoRequestDTO.getNumeroCartao());
        pagamentoExistente.setValorTotal(pagamentoRequestDTO.getValorTotal());

        return pagamentoRepository.save(pagamentoExistente);
    }
}
