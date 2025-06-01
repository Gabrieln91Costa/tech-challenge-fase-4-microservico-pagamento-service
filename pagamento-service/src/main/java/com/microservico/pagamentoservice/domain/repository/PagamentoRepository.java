package com.microservico.pagamentoservice.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservico.pagamentoservice.domain.model.Pagamento;
import com.microservico.pagamentoservice.domain.model.StatusPagamento;

import java.util.List;

@Repository
public interface PagamentoRepository extends MongoRepository<Pagamento, String> {

    List<Pagamento> findByCpfCliente(String cpfCliente);

    // Atualizado para usar StatusPagamento enum em vez de String
    List<Pagamento> findByStatus(StatusPagamento status);

    @Query("{ 'itens.sku' : ?0 }")
    List<Pagamento> findBySku(String sku);
}
