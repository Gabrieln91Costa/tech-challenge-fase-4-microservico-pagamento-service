package com.microservico.pagamentoservice.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservico.pagamentoservice.domain.dto.ItemRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.dto.PagamentoRetornoDTO;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PagamentoService pagamentoService;

    private PagamentoRequestDTO pagamentoRequestDTO;
    private ItemRequestDTO itemRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        itemRequestDTO = new ItemRequestDTO();
        itemRequestDTO.setSku("12345");
        itemRequestDTO.setQuantidade(1);
        itemRequestDTO.setPrecoUnitario(100.0);

        pagamentoRequestDTO = new PagamentoRequestDTO();
        pagamentoRequestDTO.setCpfCliente("12345678901");
        pagamentoRequestDTO.setNumeroCartao("1234 5678 9876 5432");
        pagamentoRequestDTO.setValorTotal(100.0);
        pagamentoRequestDTO.setItens(Arrays.asList(itemRequestDTO));
    }

    @Test
    void testProcessarPagamento_Sucesso() {
        when(restTemplate.getForEntity(anyString(), eq(Void.class))).thenReturn(ResponseEntity.ok().build());
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento());

        Pagamento pagamento = pagamentoService.processarPagamento(pagamentoRequestDTO);

        assertNotNull(pagamento);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void testProcessarPagamento_EstoqueIndisponivel() {
        when(restTemplate.getForEntity(anyString(), eq(Void.class)))
                .thenReturn(ResponseEntity.status(404).build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarPagamento(pagamentoRequestDTO);
        });

        assertEquals("Produto com SKU 12345 não encontrado no estoque.", exception.getMessage());
    }

    @Test
    void testProcessarRetornoPagamento_Sucesso() {
        PagamentoRetornoDTO retornoDTO = new PagamentoRetornoDTO();
        retornoDTO.setPagamentoId("123");
        retornoDTO.setStatus("SUCESSO");

        Pagamento pagamento = new Pagamento();
        pagamento.setId("123");
        pagamento.setStatus(StatusPagamento.ABERTO);

        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        Pagamento retornoPagamento = pagamentoService.processarRetornoPagamento(retornoDTO);

        assertEquals(StatusPagamento.FECHADO_COM_SUCESSO, retornoPagamento.getStatus());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void testProcessarRetornoPagamento_Falha() {
        PagamentoRetornoDTO retornoDTO = new PagamentoRetornoDTO();
        retornoDTO.setPagamentoId("123");
        retornoDTO.setStatus("FALHA");

        Pagamento pagamento = new Pagamento();
        pagamento.setId("123");
        pagamento.setStatus(StatusPagamento.ABERTO);

        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        Pagamento retornoPagamento = pagamentoService.processarRetornoPagamento(retornoDTO);

        assertEquals(StatusPagamento.FECHADO_SEM_CREDITO, retornoPagamento.getStatus());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void testProcessarRetornoPagamento_PagamentoNaoEncontrado() {
        PagamentoRetornoDTO retornoDTO = new PagamentoRetornoDTO();
        retornoDTO.setPagamentoId("123");
        retornoDTO.setStatus("SUCESSO");

        when(pagamentoRepository.findById("123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.processarRetornoPagamento(retornoDTO);
        });

        assertEquals("Pagamento não encontrado", exception.getMessage());
    }

    @Test
    void testEstornarPagamento_Sucesso() {
        Pagamento pagamentoExistente = new Pagamento();
        pagamentoExistente.setId("123");
        pagamentoExistente.setStatus(StatusPagamento.FECHADO_COM_SUCESSO);
        pagamentoExistente.setValorTotal(100.0);

        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamentoExistente));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagamento pagamentoEstornado = pagamentoService.estornarPagamento("123");

        assertNotNull(pagamentoEstornado);
        assertEquals(StatusPagamento.ESTORNADO, pagamentoEstornado.getStatus());
        assertEquals(100.0, pagamentoEstornado.getValorTotal());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void testEstornarPagamento_PagamentoNaoEncontrado() {
        when(pagamentoRepository.findById("123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.estornarPagamento("123");
        });

        assertEquals("Pagamento não encontrado", exception.getMessage());
    }

    @Test
    void testEstornarPagamento_JaEstornado() {
        Pagamento pagamentoExistente = new Pagamento();
        pagamentoExistente.setId("123");
        pagamentoExistente.setStatus(StatusPagamento.ESTORNADO);
        pagamentoExistente.setValorTotal(100.0);

        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamentoExistente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.estornarPagamento("123");
        });

        assertEquals("Pagamento já foi estornado", exception.getMessage());
    }

    @Test
    void testEstornarPagamento_StatusInvalido() {
        Pagamento pagamentoExistente = new Pagamento();
        pagamentoExistente.setId("123");
        pagamentoExistente.setStatus(StatusPagamento.ABERTO);
        pagamentoExistente.setValorTotal(100.0);

        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamentoExistente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagamentoService.estornarPagamento("123");
        });

        assertEquals("Pagamento não pode ser estornado", exception.getMessage());
    }
}
