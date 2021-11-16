package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.businessrule.util.DataUtil;

public class IdFrequenciaRonda {
	public final String idCliente;
	public final Date data;

	public IdFrequenciaRonda(String idCliente, Date data) {
		this.idCliente = idCliente;
		this.data = DataUtil.ajustarData(data);
	}

}
