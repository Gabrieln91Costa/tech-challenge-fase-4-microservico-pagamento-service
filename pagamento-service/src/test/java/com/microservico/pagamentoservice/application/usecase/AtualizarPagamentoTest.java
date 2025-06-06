package com.microservico.pagamentoservice.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;

import java.util.Optional;

class AtualizarPagamentoTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    private AtualizarPagamento atualizarPagamento;

    private PagamentoRequestDTO pagamentoRequestDTO;
    private Pagamento pagamentoExistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        atualizarPagamento = new AtualizarPagamentoImpl(pagamentoRepository);

        pagamentoRequestDTO = new PagamentoRequestDTO();
        pagamentoRequestDTO.setCpfCliente("12345678901");
        pagamentoRequestDTO.setNumeroCartao("1234 5678 9876 5432");
        pagamentoRequestDTO.setValorTotal(150.0);

        pagamentoExistente = new Pagamento();
        pagamentoExistente.setId("123");
        pagamentoExistente.setCpfCliente("12345678901");
        pagamentoExistente.setNumeroCartao("0000 0000 0000 0000");
        pagamentoExistente.setValorTotal(100.0);
    }

    @Test
    void testAtualizarPagamento_Sucesso() {
        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamentoExistente));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagamento pagamentoAtualizado = atualizarPagamento.atualizarPagamento("123", pagamentoRequestDTO);

        assertNotNull(pagamentoAtualizado);
        assertEquals(150.0, pagamentoAtualizado.getValorTotal());
        assertEquals("12345678901", pagamentoAtualizado.getCpfCliente());
        assertEquals("1234 5678 9876 5432", pagamentoAtualizado.getNumeroCartao());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void testAtualizarPagamento_PagamentoNaoEncontrado() {
        when(pagamentoRepository.findById("123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            atualizarPagamento.atualizarPagamento("123", pagamentoRequestDTO);
        });

        assertEquals("Pagamento não encontrado", exception.getMessage());
    }
}
