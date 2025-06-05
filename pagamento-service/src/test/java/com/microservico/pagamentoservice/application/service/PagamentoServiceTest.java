package com.microservico.pagamentoservice.application.service;

import com.microservico.pagamentoservice.domain.dto.ItemRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRetornoDTO;
import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PagamentoService pagamentoService;

    private final String estoqueServiceUrl = "http://estoqueservice:8080/estoque";

    // Helper para garantir itens inicializados
    private void garantirItensIniciais(Pagamento pagamento) {
        if (pagamento.getItens() == null) {
            pagamento.setItens(new ArrayList<>());
        }
    }

    @Test
    void deveCriarPagamentoComItensValidos() {
        ItemPagamento item = new ItemPagamento();
        item.setSku("sku123");
        item.setQuantidade(2);

        Pagamento pagamento = new Pagamento();
        pagamento.setItens(Collections.singletonList(item));

        when(restTemplate.getForEntity(estoqueServiceUrl + "/sku123", Void.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagamento resultado = pagamentoService.criarPagamento(pagamento);

        assertNotNull(resultado);
        assertEquals(StatusPagamento.ABERTO, resultado.getStatus());

        verify(restTemplate).getForEntity(estoqueServiceUrl + "/sku123", Void.class);
        verify(restTemplate).put(estoqueServiceUrl + "/sku123/baixar?quantidade=2", null);
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoQuandoSkuNaoExistirNoEstoque() {
        ItemPagamento item = new ItemPagamento();
        item.setSku("skuInvalido");
        item.setQuantidade(1);

        Pagamento pagamento = new Pagamento();
        pagamento.setItens(Collections.singletonList(item));

        when(restTemplate.getForEntity(estoqueServiceUrl + "/skuInvalido", Void.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pagamentoService.criarPagamento(pagamento);
        });

        assertEquals("Produto com SKU skuInvalido não encontrado no estoque.", ex.getMessage());

        verify(restTemplate).getForEntity(estoqueServiceUrl + "/skuInvalido", Void.class);
        verify(pagamentoRepository, never()).save(any());
    }

    @Test
    void deveProcessarRetornoPagamentoSucesso() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId("id123");
        pagamento.setStatus(StatusPagamento.ABERTO);
        pagamento.setItens(Collections.singletonList(new ItemPagamento()));

        when(pagamentoRepository.findById("id123")).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoRetornoDTO retornoDTO = new PagamentoRetornoDTO();
        retornoDTO.setPagamentoId("id123");
        retornoDTO.setStatus("SUCESSO");

        Pagamento resultado = pagamentoService.processarRetornoPagamento(retornoDTO);

        assertEquals(StatusPagamento.FECHADO_COM_SUCESSO, resultado.getStatus());

        verify(pagamentoRepository).findById("id123");
        verify(pagamentoRepository).save(any(Pagamento.class));
        verifyNoInteractions(restTemplate);
    }

    @Test
    void deveProcessarRetornoPagamentoFalhaEReporEstoque() {
        ItemPagamento item = new ItemPagamento();
        item.setSku("sku123");

        Pagamento pagamento = new Pagamento();
        pagamento.setId("id123");
        pagamento.setStatus(StatusPagamento.ABERTO);
        pagamento.setItens(Collections.singletonList(item));

        when(pagamentoRepository.findById("id123")).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoRetornoDTO retornoDTO = new PagamentoRetornoDTO();
        retornoDTO.setPagamentoId("id123");
        retornoDTO.setStatus("FALHA");

        Pagamento resultado = pagamentoService.processarRetornoPagamento(retornoDTO);

        assertEquals(StatusPagamento.FECHADO_SEM_CREDITO, resultado.getStatus());

        verify(restTemplate).put(estoqueServiceUrl + "/sku123/repor", null);
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void deveEstornarPagamentoFechadoComSucesso() {
        ItemPagamento item = new ItemPagamento();
        item.setSku("sku123");

        Pagamento pagamento = new Pagamento();
        pagamento.setId("id123");
        pagamento.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        pagamento.setItens(Collections.singletonList(item));

        when(pagamentoRepository.findById("id123")).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagamento resultado = pagamentoService.estornarPagamento("id123");

        assertEquals(StatusPagamento.ESTORNADO, resultado.getStatus());

        verify(restTemplate).put(estoqueServiceUrl + "/sku123/repor", null);
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoAoEstornarPagamentoNaoEncontrado() {
        when(pagamentoRepository.findById("idInvalido")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pagamentoService.estornarPagamento("idInvalido");
        });

        assertEquals("Pagamento não encontrado", ex.getMessage());
    }

    @Test
    void deveAtualizarPagamentoComSucesso() {
        Pagamento pagamentoExistente = new Pagamento();
        pagamentoExistente.setId("id123");
        pagamentoExistente.setCpfCliente("12345678900");
        pagamentoExistente.setNumeroCartao("1111222233334444");
        pagamentoExistente.setValorTotal(100.0);
        // Inicializa lista para evitar null
        pagamentoExistente.setItens(new ArrayList<>());

        when(pagamentoRepository.findById("id123")).thenReturn(Optional.of(pagamentoExistente));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoRequestDTO dto = new PagamentoRequestDTO();
        dto.setCpfCliente("09876543211");
        dto.setNumeroCartao("5555666677778888");
        dto.setValorTotal(150.0);

        ItemRequestDTO itemDTO = new ItemRequestDTO();
        itemDTO.setSku("sku999");
        itemDTO.setQuantidade(3);
        itemDTO.setPrecoUnitario(50.0);

        dto.setItens(Collections.singletonList(itemDTO));

        Pagamento atualizado = pagamentoService.atualizarPagamento("id123", dto);

        assertEquals("09876543211", atualizado.getCpfCliente());
        assertEquals("5555666677778888", atualizado.getNumeroCartao());
        assertEquals(150.0, atualizado.getValorTotal());
        assertEquals(1, atualizado.getItens().size());
        assertEquals("sku999", atualizado.getItens().get(0).getSku());

        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarPagamentoInexistente() {
        when(pagamentoRepository.findById("idInexistente")).thenReturn(Optional.empty());

        PagamentoRequestDTO dto = new PagamentoRequestDTO();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pagamentoService.atualizarPagamento("idInexistente", dto);
        });

        assertEquals("Pagamento não encontrado", ex.getMessage());
    }

}
