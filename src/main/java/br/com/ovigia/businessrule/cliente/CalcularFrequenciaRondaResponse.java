package br.com.ovigia.businessrule.cliente;

public class CalcularFrequenciaRondaResponse {
	private String idVigia;
	private String nomeVigia;
	private int totalRonda;

	public String getIdVigia() {
		return idVigia;
	}

	public void setIdVigia(String idVigia) {
		this.idVigia = idVigia;
	}

	public String getNomeVigia() {
		return nomeVigia;
	}

	public void setNomeVigia(String nomeVigia) {
		this.nomeVigia = nomeVigia;
	}

	public int getTotalRonda() {
		return totalRonda;
	}

	public void setTotalRonda(int totalRonda) {
		this.totalRonda = totalRonda;
	}
}