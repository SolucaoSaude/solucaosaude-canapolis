package com.canapolis.solucaosaude.dto;

import com.canapolis.solucaosaude.model.Paciente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    private Integer id;
    private String nome;
    private String cpf;
    private String cartaoSus;
    private String telefone;
    private Date dataNascimento;
    private String genero;
    private String ativoInativo;
    private String email;

    public PacienteDTO(Paciente paciente) {
        this.setId(paciente.getId());
        this.setNome(paciente.getNome());
        this.setCpf(paciente.getCpf());
        this.setCartaoSus(paciente.getCartaoSus());
        this.setTelefone(paciente.getTelefone());
        this.setDataNascimento(paciente.getDataNascimento());
        this.setGenero(paciente.getGenero());
        this.setAtivoInativo(paciente.getAtivoInativo());
        this.setEmail(paciente.getEmail());
    }
}
