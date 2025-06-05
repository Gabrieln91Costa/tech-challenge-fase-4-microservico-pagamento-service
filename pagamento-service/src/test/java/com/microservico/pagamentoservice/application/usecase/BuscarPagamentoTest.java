package com.microservico.pagamentoservice.application.usecase;

import com.microservico.pagamentoservice.domain.model.ItemPagamento;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarPagamentoTest {

    @Mock
    private BuscarPagamento buscarPagamento;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPagamentoPorId() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId("id123");

        when(buscarPagamento.porId("id123")).thenReturn(Optional.of(pagamento));

        Optional<Pagamento> resultado = buscarPagamento.porId("id123");

        assertTrue(resultado.isPresent());
        assertEquals("id123", resultado.get().getId());

        verify(buscarPagamento).porId("id123");
    }

    @Test
    void deveRetornarListaDePagamentosPorSku() {
        ItemPagamento item = new ItemPagamento();
        item.setSku("sku123");

        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId("p1");
        pagamento1.setItens(List.of(item));

        Pagamento pagamento2 = new Pagamento();
        pagamento2.setId("p2");
        pagamento2.setItens(List.of(item));

        List<Pagamento> pagamentos = Arrays.asList(pagamento1, pagamento2);

        when(buscarPagamento.porSku("sku123")).thenReturn(pagamentos);

        List<Pagamento> resultado = buscarPagamento.porSku("sku123");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getItens().stream().anyMatch(i -> "sku123".equals(i.getSku()))));

        verify(buscarPagamento).porSku("sku123");
    }

    @Test
    void deveListarTodosPagamentos() {
        Pagamento p1 = new Pagamento();
        p1.setId("p1");

        Pagamento p2 = new Pagamento();
        p2.setId("p2");

        List<Pagamento> todos = Arrays.asList(p1, p2);

        when(buscarPagamento.listarPagamentos()).thenReturn(todos);

        List<Pagamento> resultado = buscarPagamento.listarPagamentos();

        assertEquals(2, resultado.size());

        verify(buscarPagamento).listarPagamentos();
    }
}
