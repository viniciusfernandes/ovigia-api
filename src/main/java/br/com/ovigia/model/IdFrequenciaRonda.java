package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.businessrule.util.DataUtil;

public class IdFrequenciaRonda {
	public final String idCliente;
	public final Date dataRonda;

	public IdFrequenciaRonda(String idCliente, Date dataRonda) {
		this.idCliente = idCliente;
		this.dataRonda = DataUtil.ajustarData(dataRonda);
	}

}
