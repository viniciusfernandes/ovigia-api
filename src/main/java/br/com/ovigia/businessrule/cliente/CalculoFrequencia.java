package br.com.ovigia.businessrule.cliente;

import java.util.Date;

public class CalculoFrequencia {
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
