package com.microservico.pagamentoservice.api;

import com.microservico.pagamentoservice.application.service.PagamentoService;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRetornoDTO;
import com.microservico.pagamentoservice.domain.model.Pagamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    // Criar um novo pagamento
    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@Validated @RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
        Pagamento pagamentoCriado = pagamentoService.processarPagamento(pagamentoRequestDTO);
        return ResponseEntity.ok(pagamentoCriado);
    }

    // Buscar pagamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPagamentoPorId(@PathVariable String id) {
        return pagamentoService.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos os pagamentos
    @GetMapping
    public ResponseEntity<List<Pagamento>> listarPagamentos() {
        return ResponseEntity.ok(pagamentoService.listarPagamentos());
    }

    // Atualizar pagamento
    @PutMapping("/{id}")
    public ResponseEntity<Pagamento> atualizarPagamento(@PathVariable String id, @Validated @RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
        Pagamento pagamentoAtualizado = pagamentoService.atualizarPagamento(id, pagamentoRequestDTO);
        return ResponseEntity.ok(pagamentoAtualizado);
    }

    // Simular callback do sistema externo com status do pagamento
    @PostMapping("/retorno")
    public ResponseEntity<Pagamento> retornoPagamento(@RequestBody PagamentoRetornoDTO retornoDTO) {
        Pagamento pagamentoAtualizado = pagamentoService.processarRetornoPagamento(retornoDTO);
        return ResponseEntity.ok(pagamentoAtualizado);
    }

    // Estornar um pagamento
    @PostMapping("/{id}/estornar")
    public ResponseEntity<Pagamento> estornarPagamento(@PathVariable String id) {
        Pagamento pagamentoEstornado = pagamentoService.estornarPagamento(id);
        return ResponseEntity.ok(pagamentoEstornado);
    }
}
