package br.com.ovigia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class Localizacao {

	public Long timestamp;
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

	public Localizacao(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Localizacao(Long timestamp, Double latitude, Double longitude) {
		this(latitude, longitude);
		this.timestamp = timestamp;
	}

}
