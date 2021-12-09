package com.canapolis.solucaosaude.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PACIENTES")
public class Paciente {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(length = 250)
    private String nome;
    @Column(length = 11)
    private String cpf;
    @Column(name = "cartao_sus", length = 15)
    private String cartaoSus;
    @Column(length = 11)
    private String telefone;
    @Column(name = "data_nascimento")
    private Date dataNascimento;
    @Column(length = 1)                         // ENUM para genero
    private String genero;
    @Column(name = "ativo_inativo", length = 1) // ENUM para ativoInativo
    private String ativoInativo;
    @Column(length = 150)
    private String email;

}
