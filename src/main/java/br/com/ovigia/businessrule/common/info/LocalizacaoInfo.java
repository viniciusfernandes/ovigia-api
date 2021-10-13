package br.com.ovigia.businessrule.common.info;

public class LocalizacaoInfo {
	public Double latitude;
	public Double longitude;

	public LocalizacaoInfo() {
	}

	public LocalizacaoInfo(Double longitude, Double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
}
