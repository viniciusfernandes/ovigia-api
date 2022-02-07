package br.com.ovigia.model.enumeration;

import java.util.Arrays;
import java.util.List;

public enum TipoSituacaoChamado {
	ATIVO, ACEITO, CANCELADO_CLIENTE, CANCELADO_VIGIA, ENCERRADO;

	public static List<String> CHAMADOS_ABERTOS;
	static {
		CHAMADOS_ABERTOS = Arrays.asList(ATIVO.toString(), ACEITO.toString());
	}

	public boolean isAtivo() {
		return this == ATIVO;
	}

	public boolean isAceito() {
		return this == ACEITO;
	}
}
