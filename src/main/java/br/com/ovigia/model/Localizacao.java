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

	public double distanciaOf(Localizacao localizacao) {
		var dist = distancia(latitude, longitude, localizacao.latitude, localizacao.longitude, unidade);
		// Retornando a distancia em metros.
		return dist;

	}

	private final char unidade = 'K';

	private double distancia(double lat1, double lon1, double lat2, double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return dist;
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}
