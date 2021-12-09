package com.canapolis.solucaosaude.repository;

import com.canapolis.solucaosaude.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Paciente findByCpf(String cpf);

    @Query("FROM Paciente p WHERE p.id = ?1")
    Paciente findByPacienteId(Integer pacienteId);

    Optional<Paciente> findByCpfAndEmail(String cpf, String email);

    @Query("FROM Paciente p WHERE p.nome like %:keyword% OR p.cpf like %:keyword% OR p.email like %:keyword%")
    Page<Paciente> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("FROM Paciente p WHERE p.cpf LIKE %:cpf% OR p.cartaoSus LIKE %:cartaoSus%")
    Optional<Paciente> findByCpfCartaoSus(String cpf, String cartaoSus);

//    @Query("FROM Paciente p WHERE p.email LIKE %:email% OR p.senha LIKE %:senha%")
//    Optional<Paciente> findByEmailAndSenha(String email, String senha);
//
//    @Query("FROM Paciente p WHERE p.senha LIKE %:senha%")
//    Optional<Paciente> findBySenha(String senha);
}
