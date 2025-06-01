package com.microservico.pagamentoservice.domain.dto;

public class PagamentoRetornoDTO {

    private String pagamentoId;
    private String status; // Ex: "SUCESSO", "FALHA"

    // Getters e Setters
    public String getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(String pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
