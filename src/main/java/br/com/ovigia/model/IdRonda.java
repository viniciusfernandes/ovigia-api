package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.businessrule.util.DataUtil;

public class IdRonda {
	public final String idVigia;
	public final Date dataRonda;

	public IdRonda(String idVigia, Date data) {
		this.idVigia = idVigia;
		this.dataRonda = DataUtil.ajustarData(data);
	}

}
