package br.com.ovigia.model;

import java.util.Date;

public class IdRonda {
	public final String idVigia;
	public final Date dataRonda;

	public IdRonda(String idVigia, Date dataRonda) {
		this.idVigia = idVigia;
		this.dataRonda = dataRonda;
	}

	@Override
	public String toString() {
		return "IdRonda [idVigia=" + idVigia + ", dataRonda=" + dataRonda + "]";
	}
}
