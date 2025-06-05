package com.microservico.pagamentoservice.api;

import com.microservico.pagamentoservice.application.service.PagamentoService;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRetornoDTO;
import com.microservico.pagamentoservice.domain.exception.PagNaoEncontradoException;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import jakarta.validation.Valid; // Caso use Jakarta Validation / Bean Validation
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@Valid @RequestBody PagamentoRequestDTO request) {
        Pagamento pagamento = pagamentoService.processarPagamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPagamentoPorId(@PathVariable String id) {
        return pagamentoService.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarPagamentos() {
        List<Pagamento> pagamentos = pagamentoService.listarPagamentos();
        return ResponseEntity.ok(pagamentos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pagamento> atualizarPagamento(@PathVariable String id,
                                                        @Valid @RequestBody PagamentoRequestDTO request) {
        try {
            Pagamento pagamento = pagamentoService.atualizarPagamento(id, request);
            return ResponseEntity.ok(pagamento);
        } catch (PagNaoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/retorno")
    public ResponseEntity<Pagamento> processarRetornoPagamento(@Valid @RequestBody PagamentoRetornoDTO retornoDTO) {
        Pagamento pagamento = pagamentoService.processarRetornoPagamento(retornoDTO);
        return ResponseEntity.ok(pagamento);
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<Pagamento> estornarPagamento(@PathVariable String id) {
        try {
            Pagamento pagamento = pagamentoService.estornarPagamento(id);
            return ResponseEntity.ok(pagamento);
        } catch (PagNaoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
