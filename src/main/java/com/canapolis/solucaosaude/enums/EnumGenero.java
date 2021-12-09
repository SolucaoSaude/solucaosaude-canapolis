package com.canapolis.solucaosaude.enums;

import lombok.Getter;

public enum EnumGenero {

    MASCULINO ("M", "MASCULINO"),
    FEMININO ("F", "FEMININO");

    @Getter
    private String codigo;

    @Getter
    private String descricao;

    private EnumGenero(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
