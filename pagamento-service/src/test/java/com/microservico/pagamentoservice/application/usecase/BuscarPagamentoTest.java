package com.microservico.pagamentoservice.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

class BuscarPagamentoTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private BuscarPagamentoImpl buscarPagamento;  // Agora usa a implementação concreta

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pagamento = new Pagamento();
        pagamento.setId("123");
        pagamento.setCpfCliente("12345678901");
        pagamento.setValorTotal(100.0);
    }

    @Test
    void testBuscarPagamentoPorId_Sucesso() {
        when(pagamentoRepository.findById("123")).thenReturn(Optional.of(pagamento));

        Optional<Pagamento> resultado = buscarPagamento.porId("123");

        assertTrue(resultado.isPresent());
        assertEquals("123", resultado.get().getId());
    }

    @Test
    void testBuscarPagamentoPorId_NaoEncontrado() {
        when(pagamentoRepository.findById("123")).thenReturn(Optional.empty());

        Optional<Pagamento> resultado = buscarPagamento.porId("123");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testBuscarPagamentoPorSku_Sucesso() {
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setId("124");
        pagamento2.setCpfCliente("12345678902");
        pagamento2.setValorTotal(50.0);

        List<Pagamento> pagamentos = Arrays.asList(pagamento, pagamento2);

        when(pagamentoRepository.findBySku("12345")).thenReturn(pagamentos);

        List<Pagamento> resultados = buscarPagamento.porSku("12345");

        assertEquals(2, resultados.size());
    }

    @Test
    void testListarPagamentos_Sucesso() {
        List<Pagamento> pagamentos = Arrays.asList(pagamento);

        when(pagamentoRepository.findAll()).thenReturn(pagamentos);

        List<Pagamento> resultados = buscarPagamento.listarPagamentos();

        assertEquals(1, resultados.size());
    }
}
