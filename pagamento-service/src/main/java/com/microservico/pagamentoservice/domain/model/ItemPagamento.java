package com.microservico.pagamentoservice.domain.model;

import java.util.Objects;

public class ItemPagamento {

    private String sku;
    private Integer quantidade;
    private Double precoUnitario;

    public ItemPagamento() {
    }

    public ItemPagamento(String sku, Integer quantidade, Double precoUnitario) {
        this.sku = sku;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPagamento)) return false;
        ItemPagamento that = (ItemPagamento) o;
        return Objects.equals(sku, that.sku) &&
               Objects.equals(quantidade, that.quantidade) &&
               Objects.equals(precoUnitario, that.precoUnitario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, quantidade, precoUnitario);
    }

    @Override
    public String toString() {
        return "ItemPagamento{" +
                "sku='" + sku + '\'' +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
