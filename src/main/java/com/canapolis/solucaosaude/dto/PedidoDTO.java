package com.canapolis.solucaosaude.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Integer id;
    private String postoSaude;
    private String statusPedido;
    private String tipoConsulta;
    private Date dataPedido;

    private PacienteDTO paciente;

}
