package com.canapolis.solucaosaude.enums;

import lombok.Getter;

public enum EnumStatusPedido {

    ABERTO ("PA", "PEDIDO ABERTO"),
    MARCADA ("PM", "PEDIDO MARCADO"),
    CANCELADA ("PC", "PEDIDO CANCELADO");

    @Getter
    private String codigo;

    @Getter
    private String descricao;

    private EnumStatusPedido(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
