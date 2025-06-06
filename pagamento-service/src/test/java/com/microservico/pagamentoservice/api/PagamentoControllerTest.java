package com.microservico.pagamentoservice.api;

import com.microservico.pagamentoservice.application.service.PagamentoService;
import com.microservico.pagamentoservice.domain.dto.PagamentoRequestDTO;
import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PagamentoControllerTest {

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private PagamentoController pagamentoController;

    @Autowired
    private MockMvc mockMvc;

    private PagamentoRequestDTO pagamentoRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(pagamentoController).build();

        pagamentoRequestDTO = new PagamentoRequestDTO();
        pagamentoRequestDTO.setCpfCliente("12345678901");
        pagamentoRequestDTO.setNumeroCartao("1234123412341234");
        pagamentoRequestDTO.setValorTotal(100.00);
    }

    @Test
    void criarPagamento_DeveCriarPagamentoComSucesso() throws Exception {
        Pagamento pagamento = new Pagamento();
        pagamento.setCpfCliente(pagamentoRequestDTO.getCpfCliente());
        pagamento.setNumeroCartao(pagamentoRequestDTO.getNumeroCartao());
        pagamento.setValorTotal(pagamentoRequestDTO.getValorTotal());
        pagamento.setStatus(StatusPagamento.ABERTO);

        when(pagamentoService.processarPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);

        mockMvc.perform(post("/pagamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"cpfCliente\": \"12345678901\", \"numeroCartao\": \"1234123412341234\", \"valorTotal\": 100.00, \"itens\": [] }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpfCliente").value("12345678901"))
                .andExpect(jsonPath("$.status").value("ABERTO"));
    }

    @Test
    void buscarPagamentoPorId_SeExistirPagamento_DeveRetornarPagamento() throws Exception {
        Pagamento pagamento = new Pagamento();
        pagamento.setId("123");
        pagamento.setCpfCliente("12345678901");
        pagamento.setNumeroCartao("1234123412341234");
        pagamento.setValorTotal(100.00);
        pagamento.setStatus(StatusPagamento.ABERTO);

        when(pagamentoService.porId("123")).thenReturn(Optional.of(pagamento));

        mockMvc.perform(get("/pagamentos/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.status").value("ABERTO"));
    }

    // Outros testes de controlador podem ser adicionados aqui
}
