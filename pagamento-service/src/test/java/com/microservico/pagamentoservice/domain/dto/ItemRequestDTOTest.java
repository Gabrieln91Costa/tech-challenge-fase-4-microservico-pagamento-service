package com.microservico.pagamentoservice.domain.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDTOTest {

    private ItemRequestDTO itemRequestDTO;

    @BeforeEach
    void setup() {
        // Inicializando o DTO antes de cada teste
        itemRequestDTO = new ItemRequestDTO();
    }

    @Test
    void deveSetarEObterSkuCorretamente() {
        // Definindo o valor do SKU
        itemRequestDTO.setSku("sku001");
        
        // Verificando se o SKU foi corretamente configurado
        assertEquals("sku001", itemRequestDTO.getSku());
    }

    @Test
    void deveSetarEObterQuantidadeCorretamente() {
        // Definindo o valor da Quantidade
        itemRequestDTO.setQuantidade(10);
        
        // Verificando se a quantidade foi corretamente configurada
        assertEquals(10, itemRequestDTO.getQuantidade());
    }

    @Test
    void deveSetarEObterPrecoUnitarioCorretamente() {
        // Definindo o valor do Preço Unitário
        itemRequestDTO.setPrecoUnitario(25.50);
        
        // Verificando se o preço unitário foi corretamente configurado
        assertEquals(25.50, itemRequestDTO.getPrecoUnitario(), 0.001);
    }

    @Test
    void deveAlterarValoresCorretamente() {
        // Definindo os valores iniciais
        itemRequestDTO.setSku("sku123");
        itemRequestDTO.setQuantidade(5);
        itemRequestDTO.setPrecoUnitario(100.0);
        
        // Alterando os valores
        itemRequestDTO.setSku("sku456");
        itemRequestDTO.setQuantidade(15);
        itemRequestDTO.setPrecoUnitario(200.0);
        
        // Verificando se os valores foram alterados corretamente
        assertEquals("sku456", itemRequestDTO.getSku());
        assertEquals(15, itemRequestDTO.getQuantidade());
        assertEquals(200.0, itemRequestDTO.getPrecoUnitario(), 0.001);
    }
}
