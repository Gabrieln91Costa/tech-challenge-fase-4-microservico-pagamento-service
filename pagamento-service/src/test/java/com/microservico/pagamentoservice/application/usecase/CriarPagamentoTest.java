package com.microservico.pagamentoservice.application.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.microservico.pagamentoservice.domain.repository.PagamentoRepository;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CriarPagamentoTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private CriarPagamentoImpl criarPagamento;  // Classe concreta para injeção

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
    void testCriarPagamento_Sucesso() {
        // Preparando o mock
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        // Chamando o método
        Pagamento resultado = criarPagamento.criarPagamento(pagamento);

        // Verificando o comportamento esperado
        assertNotNull(resultado);
        assertEquals("123", resultado.getId());
        assertEquals(100.0, resultado.getValorTotal());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }
}
