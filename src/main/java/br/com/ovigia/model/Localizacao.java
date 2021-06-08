package br.com.ovigia.model;

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import reactor.core.publisher.Flux;

public class Localizacao {

	private Date hora;
	private Double latitude;
	private Double longitude;

	@JsonIgnore
	private transient Date data;
	@JsonIgnore
	private transient String idVigia;

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
