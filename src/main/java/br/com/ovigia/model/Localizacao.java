package br.com.ovigia.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Localizacao {

	public Date timestamp;
	public Double latitude;
	public Double longitude;
	public Double velocidade;
	public Double altitude;

	@JsonIgnore
	public transient Date data;
	@JsonIgnore
	public transient String idVigia;

	public Localizacao() {
	}

	public Localizacao(Date hora, Double latitude, Double longitude) {
		this.timestamp = hora;
		this.latitude = latitude;
		this.longitude = longitude;
	}

}
