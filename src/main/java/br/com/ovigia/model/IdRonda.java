package br.com.ovigia.model;

import java.util.Date;

public class IdRonda {
	private String idVigia;
	private Date data;

	public IdRonda() {
	}

	public IdRonda(String idVigia, Date data) {
		super();
		this.idVigia = idVigia;
		this.data = data;
	}

	public String getIdVigia() {
		return idVigia;
	}

	public void setIdVigia(String idVigia) {
		this.idVigia = idVigia;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
