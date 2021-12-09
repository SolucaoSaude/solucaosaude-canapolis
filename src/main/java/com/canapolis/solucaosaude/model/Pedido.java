package com.canapolis.solucaosaude.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PEDIDOS")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "posto_saude", length = 15) // ENUM para postos de saude
    private String postoSaude;
    @Column(name = "status_pedido", length = 2) // ENUM para status do pedido
    private String statusPedido;
    @Column(name = "tipo_consulta", length = 3) // ENUM para tipo de consulta
    private String tipoConsulta;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_pedido")
    private Date dataPedido;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

}
