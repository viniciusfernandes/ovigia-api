package br.com.ovigia.model.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum TipoSituacaoChamado {
    ATIVO, ACEITO, CANCELADO_CLIENTE, CANCELADO_VIGIA, ENCERRADO;

    public static Set<String> CHAMADOS_ABERTOS;

    static {
        CHAMADOS_ABERTOS = Set.of(ATIVO.toString(), ACEITO.toString());
    }

    public boolean isAberto() {
        return CHAMADOS_ABERTOS.contains(this);
    }


    public boolean isAtivo() {
        return this == ATIVO;
    }

    public boolean isAceito() {
        return this == ACEITO;
    }
}
