package com.canapolis.solucaosaude.repository;

import com.canapolis.solucaosaude.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("FROM Pedido p WHERE p.postoSaude like %:keyword% OR p.tipoConsulta like %:keyword% OR p.paciente.nome like %:keyword% OR p.paciente.cpf like %:keyword%")
    Page<Pedido> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
