package br.com.ovigia.businessrule.cliente.calcular;

import java.util.Date;

public class CalcularFrequenciaRondaRequest {
	private String idCliente;
	private Date dataRonda;

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public Date getDataRonda() {
		return dataRonda;
	}

	public void setDataRonda(Date dataRonda) {
		this.dataRonda = dataRonda;
	}
}
