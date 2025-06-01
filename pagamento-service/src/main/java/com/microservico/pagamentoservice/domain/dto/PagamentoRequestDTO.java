package com.microservico.pagamentoservice.domain.dto;

import java.util.List;

public class PagamentoRequestDTO {

    private String cpfCliente;
    private String numeroCartao;
    private double valorTotal;
    private List<ItemRequestDTO> itens;

    // Getters e Setters
    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemRequestDTO> itens) {
        this.itens = itens;
    }
}
