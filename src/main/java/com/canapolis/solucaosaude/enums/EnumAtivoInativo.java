package com.canapolis.solucaosaude.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum EnumAtivoInativo {

    ATIVO ("A", "PACIENTE ATIVO"),
    INATIVO ("I", "PACIENTE INATIVO");

    @Getter
    private String codigo;

    @Getter
    private String descricao;

    private EnumAtivoInativo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static EnumAtivoInativo valueOfCodigo(String codigo) {
        if (StringUtils.isEmpty(codigo)) {
            return null;
        }
        return Arrays.stream(EnumAtivoInativo.values())
                .filter(element -> element.getCodigo().equalsIgnoreCase(codigo))
                .findAny()
                .orElse(null);
    }
}
