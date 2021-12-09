package com.canapolis.solucaosaude.enums;

import lombok.Getter;

public enum EnumPostosSaude {

    SEDE01 ("SEDE 1", "SEDE 1"),
    SEDE02 ("SEDE 2", "SEDE 2"),
    VACA_MORTA ("VACA MORTA", "VACA MORTA"),
    SANTO_ANTONIO ("STO ANTONIO", "SANTO ANTONIO"),
    REPRESA ("REPRESA", "REPRESA");

    @Getter
    private String codigo;

    @Getter
    private String descricao;

    private EnumPostosSaude(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
