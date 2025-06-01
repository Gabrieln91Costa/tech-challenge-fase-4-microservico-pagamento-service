package com.microservico.pagamentoservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "pagamento")
public class Pagamento {

    @Id
    private String id;
    private String cpfCliente;
    private String numeroCartao;
    private Double valorTotal;
    private StatusPagamento status;

    // Lista de itens embutida no pagamento
    private List<ItemPagamento> itens;

    public Pagamento() {
    }

    public Pagamento(String id, String cpfCliente, String numeroCartao, Double valorTotal, StatusPagamento status, List<ItemPagamento> itens) {
        this.id = id;
        this.cpfCliente = cpfCliente;
        this.numeroCartao = numeroCartao;
        this.valorTotal = valorTotal;
        this.status = status;
        this.itens = itens;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public List<ItemPagamento> getItens() {
        return itens;
    }

    public void setItens(List<ItemPagamento> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pagamento)) return false;
        Pagamento that = (Pagamento) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(cpfCliente, that.cpfCliente) &&
               Objects.equals(numeroCartao, that.numeroCartao) &&
               Objects.equals(valorTotal, that.valorTotal) &&
               status == that.status &&
               Objects.equals(itens, that.itens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpfCliente, numeroCartao, valorTotal, status, itens);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id='" + id + '\'' +
                ", cpfCliente='" + cpfCliente + '\'' +
                ", numeroCartao='" + numeroCartao + '\'' +
                ", valorTotal=" + valorTotal +
                ", status=" + status +
                ", itens=" + itens +
                '}';
    }
}
