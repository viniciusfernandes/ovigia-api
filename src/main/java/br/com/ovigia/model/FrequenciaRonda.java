package br.com.ovigia.model;

import java.util.Date;

public class FrequenciaRonda {
	public String idVigia;
	public String nomeVigia;
	public int totalRonda;
	public Date dataAtualizacaoRonda;
	public Date dataUltimaRonda;

	public boolean isDesatualizada(Date dataAtualizacao) {
		return dataAtualizacaoRonda == null ? true : dataAtualizacaoRonda.compareTo(dataAtualizacao) != 0;
	}

	public boolean isExistente() {
		return dataUltimaRonda != null;
	}

}
