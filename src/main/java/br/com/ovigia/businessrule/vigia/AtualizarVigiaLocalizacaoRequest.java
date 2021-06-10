package br.com.ovigia.businessrule.vigia;

public class AtualizarVigiaLocalizacaoRequest {
	private String idVigia;
	private double latitude;
	private double longitude;

	public String getIdVigia() {
		return idVigia;
	}

	public void setIdVigia(String idVigia) {
		this.idVigia = idVigia;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
