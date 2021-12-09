package com.canapolis.solucaosaude.enums;

import lombok.Getter;

public enum EnumTipoConsulta {

    GERAL ("GER", "GERAL"),
    ESPECIALISTA ("ESP", "ESPECIALISTA");

    @Getter
    private String codigo;

    @Getter
    private String descricao;

    private EnumTipoConsulta(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
