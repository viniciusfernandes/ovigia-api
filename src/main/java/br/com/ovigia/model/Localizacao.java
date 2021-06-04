package br.com.ovigia.model;

import java.util.Date;

public class Localizacao {

	private transient Date data;
	private transient String idVigia;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getIdVigia() {
		return idVigia;
	}

	public void setIdVigia(String idVigia) {
		this.idVigia = idVigia;
	}

	private Date hora;
	private Double latitude;
	private Double longitude;

	public Localizacao() {
	}

	public Localizacao(Date hora, Double latitude, Double longitude) {
		this.hora = hora;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
